# _*_ coding: utf-8 _*_

from django import forms
from django.db import models
from django.conf import settings

from django.contrib.auth.models import User
from datetime import *

# this for mongo db 
# from mongoengine import *

# # mongo db user
# class USERS(EmbeddedDocument):
# 	user_id = stringField(max_length=50)
# 	user_pwd = stringField(max_length=50)


 

#--------------------------------------------------------------------
#--- default user ---------------------------------------
#--------------------------------------------------------------------
class USER(models.Model):
	class Meta:
		verbose_name = u'USER'
		db_table = 'USER_DB'
	# user email, id are include here 
	user = models.OneToOneField(settings.AUTH_USER_MODEL)

class USER_DATA(models.Model):
	class Meta:
		verbose_name = u'USER_DATA'
		db_table = 'USER_DATA_DB'
	# import from /django.contrib.auth.models/
	user_id = models.ForeignKey(User)
	user_points = models.IntegerField(verbose_name=u'user_points', default=0)
	user_writes = models.IntegerField(verbose_name=u'user_writes', default=0)
	# matching with write num
	user_likes = models.IntegerField(verbose_name=u'user_likes', default=0)

class USER_WRITES(models.Model):
	class Meta:
		verbose_name = u'USER_WRITES'
		db_table = 'USER_WRITES_DB'
	user_id = models.ForeignKey(USER)
	write_index = models.AutoField(verbose_name=u'write_index', primary_key=True, unique=True, db_index=True, )
	write_value = models.IntegerField(verbose_name=u'write_value', default=0, )
	write_date = models.DateTimeField(verbose_name=u'write_date', default=datetime.now, blank=True, )
	# point_time = models.DateTimeField(verbose_name=u'point_time', auto_now_add=True, blank=True)

class USER_LIKES(models.Model):
	class Meta:
		verbose_name = u'USER_LIKES'
		db_table = 'USER_LIKES_DB'
	user_id = models.ForeignKey(USER)
	wf_index = models.IntegerField(verbose_name=u'wf_index', default=0, )
	like_index = models.AutoField(verbose_name=u'like_index', primary_key=True, unique=True, db_index=True, )
	like_date = models.DateTimeField(verbose_name=u'like_date', default=datetime.now, blank=True, )
	# point_time = models.DateTimeField(verbose_name=u'point_time', auto_now_add=True, blank=True)





#--------------------------------------------------------------------
#--- food write ---------------------------------------
#--------------------------------------------------------------------

class WRITE_FRAME(models.Model):
	class Meta:
		verbose_name = u'WRITE_FRAME'
		db_table = 'WRITE_FRAME_DB'
	wf_index = models.AutoField(verbose_name=u'wf_index', primary_key=True, db_index=True, )
	wt_index = models.IntegerField(verbose_name=u'wt_index', null=False, default=0, )
	wc_index_1 = models.IntegerField(verbose_name=u'wc_index_1', null=False, default=0, )
	wc_index_2 = models.IntegerField(verbose_name=u'wc_index_2', null=False, default=0, )
	wc_index_3 = models.IntegerField(verbose_name=u'wc_index_3', null=False, default=0, )
	wc_index_4 = models.IntegerField(verbose_name=u'wc_index_4', null=False, default=0, )
	wc_index_5 = models.IntegerField(verbose_name=u'wc_index_5', null=False, default=0, )
	wc_index_6 = models.IntegerField(verbose_name=u'wc_index_6', null=False, default=0, )
	wc_index_7 = models.IntegerField(verbose_name=u'wc_index_7', null=False, default=0, )
	wc_index_8 = models.IntegerField(verbose_name=u'wc_index_8', null=False, default=0, )
	wc_index_9 = models.IntegerField(verbose_name=u'wc_index_9', null=False, default=0, )
	wc_total = models.IntegerField(verbose_name=u'wc_total', null=False, default=0, )
	wc_date = models.DateTimeField(verbose_name=u'wc_date', default=datetime.now, blank=True, )

class WRITE_TITLE(models.Model):
	class Meta:
		verbose_name = u'WRITE_TITLE'
		db_table = 'WRITE_TITLE_DB'
	wt_index = models.AutoField(verbose_name=u'wt_index', primary_key=True, db_index=True, )
	# foreign
	wf_index = models.IntegerField(verbose_name=u'wf_index', null=False, default=0, )
	# foreign
	user_id = models.IntegerField(verbose_name=u'user_id', null=False, default=0, )
	wt_name = models.CharField(verbose_name=u'wt_name', max_length=20, null=False, default='no food name', )
	# foreign
	wt_ingre = models.CharField(verbose_name=u'wt_ingre', max_length=40,)
	wt_times = models.IntegerField(verbose_name=u'wt_times', ) #소요시간(sec기준) 
	wt_quant = models.IntegerField(verbose_name=u'wt_quant',)
	# foreign
	wt_tag = models.CharField(verbose_name=u'wt_tag', max_length=40,)

class WRITE_CONTENT(models.Model):
	class Meta:
		verbose_name = u'WRITE_CONTENT' 
		db_table = 'WRITE_CONTENT_DB'
	wc_index = models.AutoField(verbose_name=u'wc_index', primary_key=True, db_index=True, )
	wt_index = models.IntegerField(verbose_name=u'wt_index', null=False, default=0, )
	wc_index_num = models.IntegerField(verbose_name=u'wc_index_num', null=False, default=0, ) # 글에서 몇번째 카드인지를 명시 
	wc_img = models.ImageField(verbose_name=u'wc_photo', upload_to='fuding_dj_s/images/content_img/', blank=True, )
	wc_text = models.CharField(verbose_name=u'wc_text', max_length=200, )
	wc_times = models.IntegerField(verbose_name=u'wc_times', null=False, default=0, )

class WRITE_TAG(models.Model):
	class Meta:
		verbose_name = u'WRITE_TAG'
		db_table = 'WRITE_TAG_DB'
	wtg_index = models.AutoField(verbose_name=u'wtg_index', primary_key=True, db_index=True, )
	wt_index = models.IntegerField(verbose_name=u'wt_index', null=False, default=0, )
	wtg_value = models.CharField(verbose_name=u'wtg_value', max_length=100, )





