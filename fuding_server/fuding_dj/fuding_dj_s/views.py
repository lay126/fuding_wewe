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

	# make title
	write_title_ = WRITE_TITLE(	user_id = user_.id,
								wt_name = wt_name, 
								wt_ingre = wt_ingre,
								wt_times = wt_times,
								wt_quant = wt_quant,
								wt_tag = wt_tag )
	write_title_.save()

	tag_w = re.compile('#\w+\s')
	tags = tag_w.findall(wt_ingre)

	tag_ = WRITE_TAG( wtg_value = tags[0])
	tag_.save()



	# code0 : success
	json_data = json.dumps(0)
	return HttpResponse(json_data, content_type='application/json')
	# json_data = json.dumps(tags)
	# return HttpResponse(json_data, content_type='application/json')



@csrf_exempt
def test_upload_write_content(request):
	return 0




