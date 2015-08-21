from django.conf.urls import patterns, include, url
from django.conf import settings
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('fuding_dj_s.views',
    # Examples:
    # url(r'^$', 'fuding_dj.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),

    # get url
    #----------------------------------------------------------------------------
	url(r'^get/newsfeed/', 'get_newsfeed'),
    url(r'^get/image/(?P<image_name>\w+/\w+/\w+.\w+)$', 'get_image'),
    url(r'^get/recipe/', 'get_recipe'),

    # upload url
    #----------------------------------------------------------------------------
	url(r'^upload/write/title/', 'test_upload_write_title'),
    url(r'^upload/write/content/', 'test_upload_write_content'),
    url(r'^upload/write/frame/', 'test_upload_write_frame'),

    # set url
    #----------------------------------------------------------------------------
    url(r'^set/like/', 'set_like'),
)

# urlpatterns += static('static_files', document_root=settings.MEDIA_ROOT)
