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
					'user_likes',
					'user_info', 
					'user_img', )

class USER_WRITESAdmin(admin.ModelAdmin):
	list_display = ('user_id',
					'write_index',
					'write_value',
					'write_date', )

class USER_LIKESAdmin(admin.ModelAdmin):
	list_display = ('user_id',
					'like_index',
					'wf_index',
					'like_date', )


class WRITE_FRAMEAdmin(admin.ModelAdmin):
	list_display = ('wf_index', 'wf_writer', 'wf_likes',
					'wt_index',
					'wc_index_1', 'wc_index_2', 'wc_index_3', 'wc_index_4',
					'wc_index_5', 'wc_index_6', 'wc_index_7', 'wc_index_8', 'wc_index_9', 
					'wc_total', 'wc_date', )

class WRITE_TITLEAdmin(admin.ModelAdmin):
	list_display = ('wt_index', 'wf_index', 
					'user_id', 'wt_name', 'wt_ingre', 'wt_times', 'wt_quant', 'wt_tag', )

class WRITE_CONTENTAdmin(admin.ModelAdmin):
	list_display = ('wc_index', 'wt_index',
					'wc_index_num', 'wc_img', 'wc_text', 'wc_times')

class WRITE_TAGAdmin(admin.ModelAdmin):
	list_display = ('wtg_index', 'wt_index', 
					'wtg_value' )





# Re-register UserAdmin
admin.site.unregister(User)
admin.site.register(User, UserAdmin)

# Register your models here.
admin.site.register(USER_DATA, USER_DATAAdmin)
admin.site.register(USER_WRITES, USER_WRITESAdmin)
admin.site.register(USER_LIKES, USER_LIKESAdmin)
admin.site.register(WRITE_FRAME, WRITE_FRAMEAdmin)
admin.site.register(WRITE_TITLE, WRITE_TITLEAdmin)
admin.site.register(WRITE_CONTENT, WRITE_CONTENTAdmin)
admin.site.register(WRITE_TAG, WRITE_TAGAdmin)


