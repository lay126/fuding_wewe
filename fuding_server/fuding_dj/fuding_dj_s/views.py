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
from django.contrib.auth.decorators import login_required

from django.contrib.auth import *
from django.contrib.auth.models import User, UserManager

from fuding_dj_s.models import *
from follow.models import *



# ------------------------------------------------------------------------------------------------------------
# USER
# ------------------------------------------------------------------------------------------------------------
@csrf_exempt
def join_user(request):
	join_id = request.POST.get('join_id', False)
	join_email = request.POST.get('join_email', False)
	join_pwd = request.POST.get('join_pwd', False)
	join_info = request.POST.get('join_info', False)

	datas = []
	dic = dict()
	try:
		join_ = User.objects.create_user(username=join_id, email=join_email, password=join_pwd)
		join_.save()
	except Exception, e:
		dic['result'] = '1'
		datas.append(dic)
		return HttpResponse(json.dumps(datas), content_type='application/json')


	join_data_ = USER_DATA(	user_id = join_, 
							user_writes = 0,
							user_likes = 0,
							user_info = join_info, )
	try:
		join_data_.save()
		dic['result'] = '0'
	except:
		dic['result'] = '1'

	datas.append(dic)
	return HttpResponse(json.dumps(datas), content_type='application/json')


@csrf_exempt
def login_user(request):
	login_id = request.POST.get('login_id')
	login_pwd = request.POST.get('login_pwd')

	datas = []
	dic = dict()

	login_ = authenticate(username=login_id, password=login_pwd)

	if login_ is not None:
		if login_.is_active:
			login(request, login_)
		else:
			#disabled account
			dic['result'] = '1'
			datas.append(dic)
			return HttpResponse(json.dumps(datas), content_type='application/json')

	else:
		#invaild login
		dic['result'] = '1'
		datas.append(dic)
		return HttpResponse(json.dumps(datas), content_type='application/json')

	#make session
	request.session['sess_id'] = login_.username

	datas = []
	dic = dict()

	# if login success, get user object.
	user_ = User.objects.get(username=login_id)
	
	dic['id'] = str(user_.id)				#index
	dic['username'] = str(user_.username)	#id
	dic['email'] = str(user_.email)

	try :
		user_data_ = USER_DATA.objects.get(user_id = user_)
		dic['user_points'] = str(user_data_.user_points)
		dic['user_writes'] = str(user_data_.user_writes)
		dic['user_info'] = str(user_data_.user_info)
	except: 
		dic['user_points'] = str(0)
		dic['user_writes'] = str(0)
		dic['user_info'] = str("")

	dic['result'] = '0'
	datas.append(dic)
	return HttpResponse(json.dumps(datas), content_type='application/json')


@csrf_exempt
def update_user(request):
	user_name = request.POST.get('user_name')
	user_info = request.POST.get('user_info')
	image_name = request.POST.get('image_name') # use, user_name

	datas = []
	dic = dict()

	try:
		user_ = User.objects.get(username=user_name)
		user_data_ = USER_DATA.objects.get(user_id=user_)
	except:
		dic['result'] = '1'
		datas.append(dic)
		return HttpResponse(json.dumps(datas), content_type='application/json')

	# save photo
 	if request.method == 'POST':
 		if 'file' in request.FILES:
 			file = request.FILES['file']
 			filename = image_name
 			try:
				user_data_.user_img.delete()
 				user_data_.user_img.save(filename, File(file), save=True)	
 			except:
 				dic['result'] = '1'
				datas.append(dic)
				return HttpResponse(json.dumps(datas), content_type='application/json')

 	if user_info is not None:
 		try: 
	 		user_data_ = USER_DATA.objects.filter(user_id=user_)
			user_data_.update(user_info=user_info)	
		except:
			dic['result'] = '1'
			datas.append(dic)
			return HttpResponse(json.dumps(datas), content_type='application/json')

	dic['result'] = '0'
	datas.append(dic)
	return HttpResponse(json.dumps(datas), content_type='application/json')


@csrf_exempt
def get_profile(request):
	user_name = request.POST.get('user_name')
	profile_name = request.POST.get('profile_name')

	datas = []
	dic = dict()

	user_ = User.objects.get(username=user_name)
	profile_user_ = User.objects.get(username=profile_name)
	profile_user_data_ = USER_DATA.objects.get(user_id=profile_user_)

	dic['user_email'] = profile_user_.email
	dic['user_name'] = profile_user_.username
	dic['user_writes'] = profile_user_data_.user_writes
	dic['user_info'] = profile_user_data_.user_info
	try:
		dic['user_img'] = profile_user_data_.user_img.url
	except:
		# 지정된 프로필 사진 없는 경우 
		dic['user_img'] = ""
	dic['user_followers'] = profile_user_data_.user_followers
	dic['user_followings'] = profile_user_data_.user_followings
	dic['follow_flag'] = ""

	datas.append(dic)
	return HttpResponse(json.dumps(datas), content_type='application/json')


# ------------------------------------------------------------------------------------------------------------
# FEED
# ------------------------------------------------------------------------------------------------------------

@csrf_exempt
def get_newsfeed(request):
	# data box
	datas = []
	try:
		user_name = request.POST.get('user_name')
		user_ = User.objects.get(username=user_name)
		user_data_ = USER_DATA.objects.get(user_id=user_)
	except:
		pass

	write_list_ =  WRITE_FRAME.objects.all().order_by('-wc_date_sort')
	follower_list_ = USER_FOLLOWS.objects.filter(user_id=user_name)

	# dict (use def)
	for d in write_list_: 
		for fl in follower_list_:
			if d.wf_writer == fl.following_id:
				dic = dict()
				dic = feed_list(user_name, d.wf_index, d.wt_index, d.wf_likes, d.wc_date, d.wf_writer, d.wc_total)
				datas.append(dic)
		if d.wf_writer == user_.username:
			dic = dict()
			dic = feed_list(user_name, d.wf_index, d.wt_index, d.wf_likes, d.wc_date, d.wf_writer, d.wc_total)
			datas.append(dic)

	json_data = json.dumps(datas)
	return HttpResponse(json_data, content_type='application/json')

# 내가 쓴 레시피 보는 부분 
@csrf_exempt
def get_myfeed(request):
	# data box
	datas = []
	try: 
		user_name = request.POST.get('user_name')
		user_ = User.objects.get(username=user_name)

		write_list_ = WRITE_FRAME.objects.filter(wf_writer=user_name)

		if len(write_list_) is 0:
			dic = dict()
			dic['result'] = "1"
			datas.append(dic)
		else:
			# dict
			for d in write_list_: 
				dic = dict()
				dic = feed_list(user_name, d.wf_index, d.wt_index, d.wf_likes, d.wc_date, d.wf_writer, d.wc_total)
				datas.append(dic)
	except:
		dic = dict()
		dic['result'] = "1"
		datas.append(dic)

	json_data = json.dumps(datas)
	return HttpResponse(json_data, content_type='application/json')

# 레시피 상세 페이지 
@csrf_exempt
def get_recipe(request):
	wf_index = request.POST.get('wf_index')

	datas = []
	dic = dict()
	
	wf_ = WRITE_FRAME.objects.get(wf_index=wf_index)
	wt_ = WRITE_TITLE.objects.get(wf_index=wf_index)
	wc_list_ = WRITE_CONTENT.objects.filter(wt_index=wt_.wt_index)

	# wf
	dic['wf_index'] = str(wf_index)
	dic['wf_writer'] = str(wf_.wf_writer)
	dic['wf_likes'] = str(wf_.wf_likes)
	dic['wc_total'] = str(wf_.wc_total)

	# wt
	dic['wt_name'] = wt_.wt_name
	dic['wt_ingre'] = wt_.wt_ingre
	dic['wt_times'] = str(wt_.wt_times)
	dic['wt_quant'] = str(wt_.wt_quant)
	dic['wt_tag'] = wt_.wt_tag

	# wc
	for wc_ in wc_list_ :
		if wf_.wc_index_1 is not 0:
			if int(wc_.wc_index_num) == 1:
				dic['wc_img_1'] = wc_.wc_img.url
				dic['wc_text_1'] = wc_.wc_text
				dic['wc_times_1'] = str(wc_.wc_times)
		if wf_.wc_index_2 is not 0:
			if int(wc_.wc_index_num) == 2:
				dic['wc_img_2'] = wc_.wc_img.url
				dic['wc_text_2'] = wc_.wc_text
				dic['wc_times_2'] = str(wc_.wc_times)
		if wf_.wc_index_3 is not 0:
			if int(wc_.wc_index_num) == 3:
				dic['wc_img_3'] = wc_.wc_img.url
				dic['wc_text_3'] = wc_.wc_text
				dic['wc_times_3'] = str(wc_.wc_times)
		if wf_.wc_index_4 is not 0:
			if int(wc_.wc_index_num) == 4:
				dic['wc_img_4'] = wc_.wc_img.url
				dic['wc_text_4'] = wc_.wc_text
				dic['wc_times_4'] = str(wc_.wc_times)
		if wf_.wc_index_5 is not 0:
			if int(wc_.wc_index_num) == 5:
				dic['wc_img_5'] = wc_.wc_img.url
				dic['wc_text_5'] = wc_.wc_text
				dic['wc_times_5'] = str(wc_.wc_times)
		if wf_.wc_index_6 is not 0:
			if int(wc_.wc_index_num) == 6:
				dic['wc_img_6'] = wc_.wc_img.url
				dic['wc_text_6'] = wc_.wc_text
				dic['wc_times_6'] = str(wc_.wc_times)
		if wf_.wc_index_7 is not 0:
			if int(wc_.wc_index_num) == 7:
				dic['wc_img_7'] = wc_.wc_img.url
				dic['wc_text_7'] = wc_.wc_text
				dic['wc_times_7'] = str(wc_.wc_times)
		if wf_.wc_index_8 is not 0:
			if int(wc_.wc_index_num) == 8:
				dic['wc_img_8'] = wc_.wc_img.url
				dic['wc_text_8'] = wc_.wc_text
				dic['wc_times_8'] = str(wc_.wc_times)
		if wf_.wc_index_9 is not 0:
			if int(wc_.wc_index_num) == 9:
				dic['wc_img_9'] = wc_.wc_img.url
				dic['wc_text_9'] = wc_.wc_text
				dic['wc_times_9'] = str(wc_.wc_times)


	datas.append(dic)

	return HttpResponse(json.dumps(datas), content_type='application/json')



# 이미지 url로 뿌림 
def get_image(request, image_name):
	link = image_name

	images = []
	image_data_ = open(link, "rb").read()
	images.append(image_data_)

	return HttpResponse(images, content_type="image/png")

# 피드 리스트를 만드는 함수 
def feed_list(user_name, wf_index, wt_index, wf_likes, wc_date, wf_writer, wc_total):
	dic = dict()
	dic['result'] = "0"
	dic['wf_index'] = str(wf_index)
	dic['wt_index'] = str(wt_index)
	dic['wf_likes'] = str(wf_likes)
	dic['wf_writer'] = str(wf_writer)
	dic['wc_date'] = str(wc_date)
	# wt_ (in dic_)
	try : 
		wt_ = WRITE_TITLE.objects.get(wf_index=wf_index)
		dic['wt_name'] = wt_.wt_name
		dic['wt_tag'] = wt_.wt_tag
	except :
		dic['wt_name'] = 'no wt_name'
		dic['wt_tag'] = 'no wt_tag'
	# wc_ (in dic_)
	wc_list_ = WRITE_CONTENT.objects.filter(wt_index=wt_index)
	for wc_ in wc_list_ :
		if wc_.wc_index_num == wc_total :
			dic['wc_img'] = wc_.wc_img.url
	# user like state 
	like_ = USER_LIKES.objects.filter(user_id=user_name).filter(wf_index=wf_index)
	if len(like_) is 0:
		# 좋아요 안된경우 
		dic['like_flag'] = '0'
	if len(like_) is not 0:
		# 이미 좋아요 된 경우
		dic['like_flag'] = '1'
	
	return dic


# ------------------------------------------------------------------------------------------------------------
# LIKE, FOLLOW
# ------------------------------------------------------------------------------------------------------------
@csrf_exempt
def set_like(request):
	user_name = request.POST.get('user_name')
	wf_index = request.POST.get('wf_index')

	like_ = USER_LIKES.objects.filter(user_id=user_name).filter(wf_index=wf_index)

	if len(like_) is 0:
		# 좋아요 해야하는 경우. 데이터 넣고, return 1
		liking_ = USER_LIKES(	user_id = user_name,
								wf_index = wf_index, )
		liking_.save()
		data = '1'
	if len(like_) is not 0:
		# 이미 좋아요 된 경우. 없애고, return 0
		for l_ in like_ :
			l_.delete()
		data = '0'

	# 0:안눌린것 1:눌린것 
	return HttpResponse(json.dumps(data), content_type='application/json')


@csrf_exempt
def set_follow(request):
	user_id = request.POST.get('user_id')
	following_id = request.POST.get('following_id')

	follow_ = USER_FOLLOWS.objects.filter(user_id=user_id).filter(following_id=following_id)

	datas = []
	if len(follow_) is 0:
		# 좋아요 해야하는 경우. 데이터 넣고, return 1
		datas = do_follow(user_id, following_id)
	if len(follow_) is not 0:
		# 이미 좋아요 된 경우. 없애고, return 0
		datas = do_unfollow(user_id, following_id)

	return HttpResponse(json.dumps(datas), content_type='application/json')

def do_follow(user_id, following_id):
	datas = []
	dic = dict()

	try: 
		user_ = User.objects.get(username = user_id)
		following_ = User.objects.get(username = following_id)
		user_data_ = USER_DATA.objects.get(user_id = user_)
		following_data_ = USER_DATA.objects.get(user_id = following_)

		# 내 팔로잉 +1, 상대방 팔로워 +1 
		try: 
			fnum_1 = user_data_.user_followings + 1
			fnum_2 = following_data_.user_followers + 1

			dic['followings'] = str(user_data_.user_followings)
			dic['followers'] = str(following_data_.user_followers)

			try:
				user_data_ = USER_DATA.objects.filter(user_id = user_)
				following_data_ = USER_DATA.objects.filter(user_id = following_)
				user_data_.update(user_followings = fnum_1)
				following_data_.update(user_followers = fnum_2)
			except:
				dic['result'] = '1'

			# 팔로우 디비에 추가 
			try: 
				follow_ = USER_FOLLOWS(	user_id = user_id,
										following_id = following_id)
				follow_.save()
				dic['result'] = '0'
				dic['like_flag'] = '1'
				
			except:
				dic['result'] = '1'
		except:
			dic['result'] = '1'
	except:
		dic['result'] = '1'

	datas.append(dic)
	return datas

def do_unfollow(user_id, following_id):
	datas = []
	dic = dict()

	try: 
		user_ = User.objects.get(username = user_id)
		following_ = User.objects.get(username = following_id)
		user_data_ = USER_DATA.objects.get(user_id = user_)
		following_data_ = USER_DATA.objects.get(user_id = following_)

		# 내 팔로잉 -1, 상대방 팔로워 -1 
		try: 
			fnum_1 = user_data_.user_followings - 1
			fnum_2 = following_data_.user_followers - 1

			dic['followings'] = str(user_data_.user_followings)
			dic['followers'] = str(following_data_.user_followers)

			try:
				user_data_ = USER_DATA.objects.filter(user_id = user_)
				following_data_ = USER_DATA.objects.filter(user_id = following_)
				user_data_.update(user_followings = fnum_1)
				following_data_.update(user_followers = fnum_2)
			except:
				dic['result'] = '1'

			# 팔로우 디비에서 삭제  
			try: 
				follow_ = USER_FOLLOWS.objects.filter(user_id=user_id).filter(following_id=following_id)
				
				if len(follow_) is not 0:
					# 이미 좋아요 된 경우. 없애고, return 0
					for f_ in follow_ :
						f_.delete()
				dic['result'] = '0'
				dic['like_flag'] = '0'
			except:
				dic['result'] = '1'
		except:
			dic['result'] = '1'
	except:
		dic['result'] = '1'

	datas.append(dic)
	return datas


# ------------------------------------------------------------------------------------------------------------
# WRITE
# ------------------------------------------------------------------------------------------------------------
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
	wt_date = request.POST.get('wt_date')
	wc_total = request.POST.get('wc_total') # 해당 카드의 갯수 

	def __unicode__(self):
		return u'%s %s %s' % (self.user_name, self.wc_photo_name)

	user_ = User.objects.get(username=user_name)
	wt_ = WRITE_TITLE.objects.filter(wt_index=wt_index)

	# make frame object
	write_frame_ = WRITE_FRAME(	wt_index = wt_index,
								wc_total = wc_total, 
								wf_writer = user_name,)
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

	wf_.update(wc_date_sort=wt_date)

	json_data = json.dumps(write_frame_.wf_index)
	return HttpResponse(json_data, content_type='application/json')


# ------------------------------------------------------------------------------------------------------------
# SEARCH
# ------------------------------------------------------------------------------------------------------------
# 해쉬태그들을 tagDB에 저장하는 함수 (공통사용)
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

	return 0


# 해쉬태그 검색 함수 
@csrf_exempt
def hash_find(request):
	search_text = request.POST.get('search_text')

	hash_w = re.compile('#\w*[^ \u3131-\u3163*\uac00-\ud7a3*]*\w*[^ \u3131-\u3163*\uac00-\ud7a3*]*')
	hashs = hash_w.findall(search_text)

	datas = []
	check_data = []
	for h in hashs:
		tag_frames = WRITE_TAG.objects.filter(wtg_value=h)
		if len(tag_frames) is 0:
			dic = dict()
			dic['result'] = "1"
			datas.append(dic)
		else:
			dic = dict()
			dic['result'] = "0"
			datas.append(dic)
			for tag_frame in tag_frames:
				# 중복 데이터 출력 방지 
				wt_index_ = tag_frame.wt_index
				if wt_index_ not in check_data:
					check_data.append(wt_index_)
					try :
						wt_ = WRITE_TITLE.objects.get(wt_index=wt_index_)
						wf_ = WRITE_FRAME.objects.get(wf_index=wt_.wf_index)
						wc_list_ = WRITE_CONTENT.objects.filter(wt_index=wf_.wt_index)

						dic = dict()
						dic['wf_writer'] = str(wf_.wf_writer)
						dic['wf_index'] = str(wf_.wf_index)
						dic['wt_index'] = str(wf_.wt_index)
						dic['wf_likes'] = str(wf_.wf_likes)
						dic['wc_date'] = str(wf_.wc_date)

						# wt_ (in dic_)
						dic['wt_name'] = wt_.wt_name
						dic['wt_tag'] = wt_.wt_tag

						# wc_ (in dic_)
						for wc_ in wc_list_ :
							if wc_.wc_index_num == wf_.wc_total :
								dic['wc_img'] = wc_.wc_img.url

						# user like state 
						like_ = USER_LIKES.objects.filter(user_id=user_name).filter(wf_index=d.wf_index)
						if len(like_) is 0:
							# 좋아요 안된경우 
							dic['like_flag'] = '0'
						if len(like_) is not 0:
							# 이미 좋아요 된 경우
							dic['like_flag'] = '1'

						datas.append(dic)
					except :
						pass

	json_data = json.dumps(datas)
	return HttpResponse(json_data, content_type='application/json')







