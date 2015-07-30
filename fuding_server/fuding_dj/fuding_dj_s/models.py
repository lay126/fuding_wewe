# _*_ coding: utf-8 _*_

from django import forms
from django.db import models
from django.conf import settings

# this for mongo db 
from mongoengine import *



# Create your models here.


# # mongo db user
# class USERS(EmbeddedDocument):
# 	user_id = stringField(max_length=50)
# 	user_pwd = stringField(max_length=50)


# default user
class USER(models.Model):
	class Meta:
		verbose_name = u'USER'
		db_table = 'USER_DB'
	user = models.OneToOneField(settings.AUTH_USER_MODEL)

class USER_DATA(models.Model):
	class Meta:
		verbose_name = u'USER_DATA'
		db_table = 'USER_DATA_DB'
	user_id = models.ForeignKey(User)
	user_email = models.CharField(verbose_name=u'user_email', null=False, max_length=100)
	user_name = models.CharField(verbose_name=u'user_name', null=False, max_length=10)
	user_points = models.IntegerField(verbose_name=u'user_points', default=0)
	user_writes = models.IntegerField(verbose_name=u'user_writes', default=0)
	# matching with write num
	user_likes = models.CharField(verbose_name=u'user_likes', default=0)





	