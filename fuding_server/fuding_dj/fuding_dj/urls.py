from django.conf.urls import patterns, include, url
from django.conf import settings
from django.contrib import admin

admin.autodiscover()

urlpatterns = patterns('',

    url(r'^admin/', include(admin.site.urls)),

    # user url
    #----------------------------------------------------------------------------
    url(r'^join/user', 'fuding_dj_s.views.join_user'),
    url(r'^login/user', 'fuding_dj_s.views.login_user'),
    url(r'^update/user', 'fuding_dj_s.views.update_user'),

    # get url
    #----------------------------------------------------------------------------
	url(r'^get/newsfeed/', 'fuding_dj_s.views.get_newsfeed'),
    url(r'^get/myfeed/', 'fuding_dj_s.views.get_myfeed'),
    url(r'^get/image/(?P<image_name>\w+/\w+/\w+.\w+)$', 'fuding_dj_s.views.get_image'),
    url(r'^get/recipe/', 'fuding_dj_s.views.get_recipe'),
    url(r'^get/profile/', 'fuding_dj_s.views.get_profile'),
    url(r'^get/noti/', 'fuding_dj_s.views.get_noti'),

    url(r'^hash/find/', 'fuding_dj_s.views.hash_find'),

    # upload url
    #----------------------------------------------------------------------------
	url(r'^upload/write/title/', 'fuding_dj_s.views.upload_write_title'),
    url(r'^upload/write/content/', 'fuding_dj_s.views.upload_write_content'),
    url(r'^upload/write/frame/', 'fuding_dj_s.views.upload_write_frame'),
    url(r'^upload/write/comment/', 'fuding_dj_s.views.upload_write_comment'),

    # set url
    #----------------------------------------------------------------------------
    url(r'^set/like/', 'fuding_dj_s.views.set_like'),
    url(r'^set/follow/', 'fuding_dj_s.views.set_follow'),


    # follow api
    #----------------------------------------------------------------------------
    url(r'^toggle/(?P<app>[^\/]+)/(?P<model>[^\/]+)/(?P<id>\d+)/$', 'follow.views.toggle', name='toggle'),
    url(r'^follow/(?P<app>[^\/]+)/(?P<model>[^\/]+)/(?P<id>\d+)/$', 'follow.views.toggle', name='follow'),
    url(r'^unfollow/(?P<app>[^\/]+)/(?P<model>[^\/]+)/(?P<id>\d+)/$', 'follow.views.toggle', name='unfollow'),
    url(r'^followaa/(?P<app>[^\/]+)/(?P<model>[^\/]+)/(?P<id>\d+)/$', 'follow.views.toggleaa', name='aa'),
)