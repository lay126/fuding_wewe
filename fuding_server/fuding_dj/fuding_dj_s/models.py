# _*_ coding: utf-8 _*_

from django import forms
from django.db import models
from django.conf import settings

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
	user_id = models.ForeignKey(USER)
	user_points = models.IntegerField(verbose_name=u'user_points', default=0)
	user_writes = models.IntegerField(verbose_name=u'user_writes', default=0)
	# matching with write num
	user_likes = models.IntegerField(verbose_name=u'user_likes', default=0)

class USER_POINTS(models.Model):
	class Meta:
		verbose_name = u'USER_POINTS'
		db_table = 'USER_POINTS_DB'
	user_id = models.ForeignKey(USER)
	point_index = models.AutoField(verbose_name=u'point_index', primary_key=True, unique=True, db_index=True, )
	point_value = models.IntegerField(verbose_name=u'point_value', default=0, )
	point_time = models.DateTimeField(verbose_name=u'point_time', default=datetime.now, blank=True, )
	# point_time = models.DateTimeField(verbose_name=u'point_time', auto_now_add=True, blank=True)














