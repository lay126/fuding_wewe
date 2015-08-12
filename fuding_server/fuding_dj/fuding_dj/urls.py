from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'fuding_dj.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),

    # get url
    #----------------------------------------------------------------------------
	url(r'^get/newsfeed/', 'fuding_dj_s.views.get_newsfeed'),
    url(r'^get/image/', 'fuding_dj_s.views.get_image'),

    # upload url
    #----------------------------------------------------------------------------
	url(r'^upload/write/title/', 'fuding_dj_s.views.test_upload_write_title'),
    url(r'^upload/write/content/', 'fuding_dj_s.views.test_upload_write_content'),
    url(r'^upload/write/frame/', 'fuding_dj_s.views.test_upload_write_frame'),
)
