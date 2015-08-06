# _*_ encoding: utf-8 _*_
import json
import re

from django import forms
from django.shortcuts import *
from django.http import *
from django.core.context_processors import *
from django.forms.models import *
from django.template import *
from django.utils.simplejson import dumps, loads, JSONEncoder
# from django.utils import simplejson
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

	def __unicode__(self):
		return u'%s' % (self.user_name)

	user_ = User.objects.get(username=user_name)

	# 일단 여은이 test용으로 content 디비 사용. 추후에는 db데이터들 조합하여 보내줌 
	# DB.objects.filter(a = a)

	# # get data from old image (as you already did)
	# data = list(oldimg.getdata())
	# # create empty new image of appropriate format
	# newimg = Image.new(format, size)  # e.g. ('RGB', (640, 480))
	# # insert saved data into the image
	# newimg.putdata(data)

	# data 조합을 위한 class 생성
	class WR:
		def __init__(self, wr_name, wr_date, wr_image, wr_likes, wr_tags):
			self.wr_name = wr_name
			self.wr_date = wr_date
			self.wr_image = wr_image
			self.wr_likes = wr_likes
			self.wr_tags = wr_tags

	datas = []
	write_list_ =  WRITE_CONTENT.objects.all()

	for d in write_list_:
		data = model_to_dict(d)
		datas.append(data)

	wr_ = WR('me', '2014', write_list_[5].wc_img, 10, '#tagggg')

	json_data = json.dumps(unicode(wr_.wr_image))
	return HttpResponse(json_data, content_type='application/json')



@csrf_exempt
def test_upload_write_title(request):

	user_name = request.POST.get('user_name')
	wt_name = request.POST.get('wt_name')
	wt_ingre = request.POST.get('wt_ingre')
	wt_times = request.POST.get('wt_times')
	wt_quant = request.POST.get('wt_quant')
	wt_tag = request.POST.get('wt_tag')

	def __unicode__(self):
		return u'%s %s %s' % (self.wt_name, self.wt_ingre, self.wt_tag)

	user_ = User.objects.get(username=user_name)

	# make title object
	write_title_ = WRITE_TITLE(	user_id = user_.id,
								wt_name = wt_name, 
								wt_ingre = wt_ingre,
								wt_times = wt_times,
								wt_quant = wt_quant,
								wt_tag = wt_tag )
	write_title_.save()

	# 지금 한글은 안됨 0806
	hash_w = re.compile('#\w+')
	# hash_w = re.compile('#[가-힣]+')

	hashs = hash_w.findall(wt_ingre)
	hashs2 = hash_w.findall(wt_tag)

	for h in hashs:
		hash_ = WRITE_TAG(	wtg_value = h,
							wt_index = write_title_.wt_index )
	hash_.save()

	for h in hashs2:
		hash_ = WRITE_TAG(	wtg_value = h,
							wt_index = write_title_.wt_index )
	hash_.save()

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
	wc_finish_index = request.POST.get('wc_finish_index') # 끝이라면 1 주세여 

	def __unicode__(self):
		return u'%s %s %s' % (self.user_name, self.wc_photo_name, self.wc_text)

	user_ = User.objects.get(username=user_name)

	# make title object
	write_content_ = WRITE_CONTENT(	wt_index = wt_index,
									wc_index_num = wc_index_num,
									wc_text = wc_text,
									wc_times = wc_times )
	write_content_.save()

	# 지금 한글은 안됨 0806
	hash_w = re.compile('#\w+')
	hashs = hash_w.findall(wc_text)

	for h in hashs:
		hash_ = WRITE_TAG(	wtg_value = h,
							wt_index = write_content_.wt_index )
	hash_.save()

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

	json_data = json.dumps(write_content_.wt_index)
	return HttpResponse(json_data, content_type='application/json')


