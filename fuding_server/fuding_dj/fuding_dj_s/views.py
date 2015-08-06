# _*_ coding: utf-8 _*_
import json
import re

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

	# 해쉬태그 단어를 꺼내오기 위한 정규식. 
	tag_w = re.compile('#\w+')
	tags = tag_w.findall(wt_ingre)

	# 몰랐던 사실, write_title_객체 생성시에 wt_index는 없었으나,
	# .save() 된 뒤에 DB접근을 다시 하지 않더라도 해당 객체의 다른 값들을 꺼내 쓸수 있음..
	#### 신기방기. 똑똑하다. 
	#### -> db_index값인 wt_index을 꺼내오려고 DB에 다른 unique한 값을 설정해야 하나 고민 중이었음
	for t in tags:
		tag_ = WRITE_TAG(	wtg_value = t,
							wt_index = write_title_.wt_index )
		tag_.save()


	json_data = json.dumps(len(tags))
	return HttpResponse(json_data, content_type='application/json')



@csrf_exempt
def test_upload_write_content(request):
	return 0




