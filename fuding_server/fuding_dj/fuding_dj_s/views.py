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
	# return HttpResponse(wt_tag)



@csrf_exempt
def test_upload_write_content(request):
	return 0




