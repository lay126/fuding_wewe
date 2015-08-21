# _*_ encoding: utf-8 _*_
import json
import re
import base64
import sys

from django import forms
from django.shortcuts import *
from django.http import *
from django.core.context_processors import *
from django.forms.models import *
from django.template import *
from django.utils.simplejson import dumps, loads, JSONEncoder
from django.core.files import File
from django.core.context_processors import *
from django.views.decorators.csrf import *
from django.core.files.storage import default_storage
from django.contrib.sessions.models import Session

from django.contrib.auth import *
from django.contrib.auth.models import User, UserManager

from fuding_dj_s.models import *


@csrf_exempt
def get_newsfeed(request):
	user_name = request.POST.get('user_name')
	user_ = User.objects.get(username=user_name)

	# data box
	datas = []
	write_list_ =  WRITE_FRAME.objects.all()

	# dict
	for d in write_list_: 
		dic = dict()
		dic['wf_index'] = str(d.wf_index)
		dic['wt_index'] = str(d.wt_index)
		dic['wf_likes'] = str(d.wf_likes)
		dic['wc_date'] = str(d.wc_date)
		# wt_ (in dic_)
		try : 
			wt_ = WRITE_TITLE.objects.get(wf_index=d.wf_index)
			dic['wt_name'] = wt_.wt_name
			dic['wt_tag'] = wt_.wt_tag
		except :
			dic['wt_name'] = 'no wt_name'
			dic['wt_tag'] = 'no wt_tag'
		# wc_ (in dic_)
		wc_list_ = WRITE_CONTENT.objects.filter(wt_index=d.wt_index)
		for wc_ in wc_list_ :
			if wc_.wc_index_num == d.wc_total :
				dic['wc_img'] = wc_.wc_img.url
		datas.append(dic)

	json_data = json.dumps(datas)
	return HttpResponse(json_data, content_type='application/json')


def get_image(request, image_name):
	link = image_name

	images = []
	image_data_ = open(link, "rb").read()
	images.append(image_data_)

	return HttpResponse(images, content_type="image/png")


''' 
개당 레시피 보내주는 방식을 아직 모름.
frame -> t/c -> content 순으로 보이는 건가?
알아야지 짤 수 있을듯.
'''
@csrf_exempt
def get_recipe(request):
	wf_index = request.POST.get('wf_index')

	datas = []
	dic = dict()
	di = dict()

	dic['wf_index'] = str(wf_index)
	wt_ = WRITE_TITLE.objects.get(wf_index=wf_index)
	dic['wt_index'] = str(wt_.wt_index)
	# testing
	di['aa'] = 'aa'
	wc_list_ = WRITE_CONTENT.objects.filter(wt_index=wt_.wt_index)
	datas.append(dic)
	datas.append(di)

	json_data = json.dumps(datas)
	return HttpResponse(json_data, content_type='application/json')


def set_like(request):

	# 0:안눌린것 1:눌린것 
	return 1;


@csrf_exempt
def test_upload_write_title(request):

	user_name = request.POST.get('user_name')
	wt_name = request.POST.get('wt_name')
	wt_ingre = request.POST.get('wt_ingre')
	wt_times = request.POST.get('wt_times')
	wt_quant = request.POST.get('wt_quant')
	wt_tag = request.POST.get('wt_tag')

	user_ = User.objects.get(username=user_name)

	def __unicode__(self):
		return u'%s %s %s' % (self.wt_name, self.wt_ingre, self.wt_tag)

	# make title object
	write_title_ = WRITE_TITLE(	user_id = user_.id,
								wt_name = wt_name, 
								wt_ingre = wt_ingre,
								wt_times = wt_times,
								wt_quant = wt_quant,
								wt_tag = wt_tag )
	write_title_.save()

	# HASH TAG DEF
	hash_tag_make(wt_ingre, write_title_.wt_index)
	hash_tag_make(wt_tag, write_title_.wt_index)

	json_data = json.dumps(write_title_.wt_index)
	return HttpResponse(json_data, content_type='application/json')


@csrf_exempt
def test_upload_write_content(request):

	user_name = request.POST.get('user_name')
	wt_index = request.POST.get('wt_index')
	wc_index_num = request.POST.get('wc_index_num') # 해당 카드의 순서 번호 
	wc_photo_name = request.POST.get('wc_photo_name') # 사진 이름을 지정하기위한 파람 
	wc_text = request.POST.get('wc_text')
	wc_times = request.POST.get('wc_times')
	wc_finish_index = request.POST.get('wc_finish_index') # 끝이라면 1 주세여  # 이제 필요 없음 

	def __unicode__(self):
		return u'%s %s %s' % (self.user_name, self.wc_photo_name, self.wc_text)

	user_ = User.objects.get(username=user_name)

	# make title object
	write_content_ = WRITE_CONTENT(	wt_index = wt_index,
									wc_index_num = wc_index_num,
									wc_text = wc_text,
									wc_times = wc_times )
	write_content_.save()

	# HASH TAG DEF
	hash_tag_make(wc_text, wt_index)

	# save photo
	if request.method == 'POST':
		if 'file' in request.FILES:
			file = request.FILES['file']
			filename = wc_photo_name

			try:
				write_content_.wc_img.save(filename, File(file), save=True)	
			except:
				# code1 : save photo fail
				json_data = json.dumps('save photo fail')
				return HttpResponse(json_data, content_type='application/json')	

	# 0 : success
	json_data = json.dumps(write_content_.wt_index)
	return HttpResponse(json_data, content_type='application/json')


@csrf_exempt
def test_upload_write_frame(request):

	user_name = request.POST.get('user_name')
	wt_index = request.POST.get('wt_index')
	wc_total = request.POST.get('wc_total') # 해당 카드의 갯수 
	wc_photo_name = request.POST.get('wc_photo_name') # 사진 이름을 지정하기위한 파람 

	def __unicode__(self):
		return u'%s %s %s' % (self.user_name, self.wc_photo_name)

	user_ = User.objects.get(username=user_name)
	wt_ = WRITE_TITLE.objects.filter(wt_index=wt_index)

	# make frame object
	write_frame_ = WRITE_FRAME(	wt_index = wt_index,
								wc_total = wc_total, )
	write_frame_.save()

	# update titleDB.wf_index 
	wt2_ = WRITE_TITLE.objects.filter(wt_index=wt_index)
	wt2_.update(wf_index=write_frame_.wf_index)

	# update frameDB.wc_index_... each
	wc_list_ = WRITE_CONTENT.objects.filter(wt_index=wt_index)

	wf_ = WRITE_FRAME.objects.filter(wf_index=write_frame_.wf_index)
	wf_.update(wc_index_1=10)

	# if len(wc_list_) == int(wc_total):
	for wc_ in wc_list_:
		if int(wc_.wc_index_num) == 1:
			wf_.update(wc_index_1=wc_.wc_index)
		if int(wc_.wc_index_num) == 2:
			wf_.update(wc_index_2=wc_.wc_index)
		if int(wc_.wc_index_num) == 3:
			wf_.update(wc_index_3=wc_.wc_index)
		if int(wc_.wc_index_num) == 4:
			wf_.update(wc_index_4=wc_.wc_index)
		if int(wc_.wc_index_num) == 5:
			wf_.update(wc_index_5=wc_.wc_index)
		if int(wc_.wc_index_num) == 6:
			wf_.update(wc_index_6=wc_.wc_index)
		if int(wc_.wc_index_num) == 7:
			wf_.update(wc_index_7=wc_.wc_index)
		if int(wc_.wc_index_num) == 8:
			wf_.update(wc_index_8=wc_.wc_index)
		if int(wc_.wc_index_num) == 9:
			wf_.update(wc_index_9=wc_.wc_index)

	# save photo
	if request.method == 'POST':
		if 'file' in request.FILES:
			file = request.FILES['file']
			filename = wc_photo_name

			try:
				write_frame_.wc_img.save(filename, File(file), save=True)	
			except:
				json_data = json.dumps('save photo fail')
				return HttpResponse(json_data, content_type='application/json')	

	json_data = json.dumps(write_frame_.wf_index)
	return HttpResponse(json_data, content_type='application/json')


def hash_tag_make(hash_text, wt_index):
	# 한글 가능 : 해시태그 작성시에 띄어쓰기 해주세요 ㅠㅠ
	hash_w = re.compile('#\w*[^ \u3131-\u3163*\uac00-\ud7a3*]*\w*[^ \u3131-\u3163*\uac00-\ud7a3*]*')
	hashs = hash_w.findall(hash_text)

	try : 
		for h in hashs:
			hash_ = WRITE_TAG(	wtg_value = h,
								wt_index = wt_index )
			hash_.save()
	except : 
		hashs = hash_w.findall(wc_text)

	return 0;

