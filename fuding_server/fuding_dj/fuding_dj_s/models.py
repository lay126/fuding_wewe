# _*_ coding: utf-8 _*_

from django import forms
from django.db import models
from django.conf import settings




# Create your models here.


# default user
class USERS(models.Model):
	class Meta:
		verbose_name = u'USERS'
		db_table = 'USERS_DB'
	users = models.OneToOneField(settings.AUTH_USER_MODEL)

class USER_DATA(models.Model):
	class Meta:
		verbose_name = u'USER_DATA'
		db_table = 'USER_DATA_DB'
	user_id = models.ForeignKey(Users)
	user_email = models.CharField(verbose_name=u'user_email', null=False, max_length=100)
	user_name = models.CharField(verbose_name=u'user_name', null=False, max_length=10)
	user_points = models.IntegerField(verbose_name=u'user_points', default=0)
	user_writes = models.IntegerField(verbose_name=u'user_writes', default=0)
	# matching with write num
	user_likes = models.CharField(verbose_name=u'user_likes', default=0)