# _*_ encoding: utf-8 _*_
# for 한글 

from django.contrib import admin
from django import forms

from django.contrib.auth.models import User, UserManager
from django.contrib.auth.admin import UserAdmin

from fuding_dj_s.models import *


# Register your models here.

class USERInline(admin.StackedInline):
	model = USER
	can_delete = False
	verbose_name_plural = 'USER'

class UserAdmin(admin.ModelAdmin):
	inlines = (USERInline, )
	list_display = ('id', 'username', 'email', 'password', )



class USER_DATAAdmin(admin.ModelAdmin):
	list_display = ('user_id',
					'user_points',
					'user_writes',
					'user_likes', )

class USER_POINTSAdmin(admin.ModelAdmin):
	list_display = ('user_id',
					'point_index',
					'point_value',
					'point_time', )



# Re-register UserAdmin
admin.site.unregister(User)
admin.site.register(User, UserAdmin)

# Register your models here.
admin.site.register(USER_DATA, USER_DATAAdmin)
admin.site.register(USER_POINTS, USER_POINTSAdmin)

