# _*_ coding: utf-8 _*_

from django import forms
from django.db import models
from django.conf import settings

from django.contrib.auth.models import User
from datetime import *

from follow import utils



# --------------------------------------------------------------------
# user
# --------------------------------------------------------------------
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
	user_writes = models.IntegerField(verbose_name=u'user_writes', default=0)
	# matching with write num
	user_likes = models.IntegerField(verbose_name=u'user_likes', default=0)
	user_info = models.CharField(verbose_name=u'user_info', default='', max_length=100,)
	user_img = models.ImageField(verbose_name=u'user_img', upload_to='fuding_dj_s/images/', blank=True, )
	user_followers = models.IntegerField(verbose_name=u'user_followers', default=0)
	user_followings = models.IntegerField(verbose_name=u'user_followings', default=0)


class USER_WRITES(models.Model):
	class Meta:
		verbose_name = u'USER_WRITES'
		db_table = 'USER_WRITES_DB'
	user_id = models.CharField(verbose_name=u'user_id', default='no name', max_length=20,)
	write_index = models.AutoField(verbose_name=u'write_index', primary_key=True, unique=True, db_index=True, )
	write_value = models.IntegerField(verbose_name=u'write_value', default=0, )
	write_date = models.DateTimeField(verbose_name=u'write_date', default=datetime.now, blank=True, )

class USER_LIKES(models.Model):
	class Meta:
		verbose_name = u'USER_LIKES'
		db_table = 'USER_LIKES_DB'
	user_id = models.CharField(verbose_name=u'user_id', default='no name', max_length=20,)
	wf_index = models.IntegerField(verbose_name=u'wf_index', default=0, )
	like_index = models.AutoField(verbose_name=u'like_index', primary_key=True, unique=True, db_index=True, )
	like_date = models.DateTimeField(verbose_name=u'like_date', default=datetime.now, blank=True, )

class USER_FOLLOWS(models.Model):
	class Meta:
		verbose_name = u'USER_FOLLOWS'
		db_table = 'USER_FOLLOWS_DB'
	user_id = models.CharField(verbose_name=u'user_id', default='no name', max_length=20,)
	following_id = models.CharField(verbose_name=u'following_id', default='no name', max_length=20,)

class USER_NOTIS(models.Model):
	class Meta:
		verbose_name = u'USER_NOTIS'
		db_table = 'USER_NOTIS_DB'
	noti_index = models.AutoField(verbose_name=u'noti_index', primary_key=True, db_index=True, )
	user_id = models.CharField(verbose_name=u'user_id', default='no name', max_length=20,)
	# 활동을 취한 사람의 아이디 
	noti_id = models.CharField(verbose_name=u'noti_id', default='no name', max_length=20,)
	# 활동이 취해진 글 번호 -> 팔로우인 경우 0을 취한다.
	wf_index = models.IntegerField(verbose_name=u'wf_index', null=False, default=0)
	# 1: 좋아요, 2: 댓글, 3: 팔로우 
	noti_flag = models.IntegerField(verbose_name=u'noti_flag', default=0)
	# 0: not read, 1: read
	noti_read = models.IntegerField(verbose_name=u'noti_read', default=0)
	# 좋아요, 댓글, 팔로우 시에 하나씩 추가해서 보내주어야 함 !
	noti_date = models.CharField(verbose_name=u'noti_date', max_length=20, null=False, default='yyyy/mm/dd', )
	noti_date_sort = models.DateTimeField(verbose_name=u'noti_date_sort', default=datetime.now, blank=True, )


# --------------------------------------------------------------------
# food write 
# --------------------------------------------------------------------
class WRITE_FRAME(models.Model):
	class Meta:
		verbose_name = u'WRITE_FRAME'
		db_table = 'WRITE_FRAME_DB'
	wf_index = models.AutoField(verbose_name=u'wf_index', primary_key=True, db_index=True, )
	wf_writer = models.CharField(verbose_name=u'wf_writer', max_length=20, null=False, default='no name', )
	wf_likes = models.IntegerField(verbose_name=u'wf_likes', null=False, default=0, )
	wf_comments = models.IntegerField(verbose_name=u'wf_comments', null=False, default=0, )
	wt_index = models.IntegerField(verbose_name=u'wt_index', null=False, default=0, unique=True, )
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
	wc_date = models.CharField(verbose_name=u'wc_date', max_length=20, null=False, default='yyyy/mm/dd', )
	wc_date_sort = models.DateTimeField(verbose_name=u'wc_date_sort', default=datetime.now, blank=True, )

class WRITE_TITLE(models.Model):
	class Meta:
		verbose_name = u'WRITE_TITLE'
		db_table = 'WRITE_TITLE_DB'
	wt_index = models.AutoField(verbose_name=u'wt_index', primary_key=True, db_index=True, )
	# foreign
	wf_index = models.IntegerField(verbose_name=u'wf_index', null=False, default=0)
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
	wc_img = models.ImageField(verbose_name=u'wc_img', upload_to='fuding_dj_s/images/', blank=True, )
	wc_text = models.CharField(verbose_name=u'wc_text', max_length=200, )
	wc_times = models.IntegerField(verbose_name=u'wc_times', null=False, default=0, )

class WRITE_COMMENT(models.Model):
	class Meta:
		verbose_name = u'WRITE_COMMENT' 
		db_table = 'WRITE_COMMENT_DB'
	wcm_index = models.AutoField(verbose_name=u'wcm_index', primary_key=True, db_index=True, )
	wt_index = models.IntegerField(verbose_name=u'wt_index', null=False, default=0, )
	wcm_writer = models.CharField(verbose_name=u'wcm_writer', max_length=20, null=False, default='no name', )
	wcm_text = models.CharField(verbose_name=u'wcm_text', max_length=200, )
	wcm_date = models.DateTimeField(verbose_name=u'wc_date', default=datetime.now, blank=True, )

class WRITE_TAG(models.Model):
	class Meta:
		verbose_name = u'WRITE_TAG'
		db_table = 'WRITE_TAG_DB'
	wtg_index = models.AutoField(verbose_name=u'wtg_index', primary_key=True, db_index=True, )
	wt_index = models.IntegerField(verbose_name=u'wt_index', null=False, default=0, )
	wtg_value = models.CharField(verbose_name=u'wtg_value', max_length=100, )






