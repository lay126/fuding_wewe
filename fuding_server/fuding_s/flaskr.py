# -*- coding: UTF-8 -*-
# 한글 사용을 위한 인코딩 설정 

# 데이터베이스 사용을 위한 부분
from __future__ import with_statement
from contextlib import closing

# all the imports
import sqlite3
from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash
from jinja2 import Template

from tornado.wsgi import WSGIContainer
from tornado.httpserver import HTTPServer
from tornado.ioloop import IOLoop 


# configuration
DATABASE = '/tmp/flaskr.db'
DEBUG = True
SECRET_KEY = 'development key'
USERNAME = 'admin'
PASSWORD = 'default'

# create our little application :)
# 실제 어플리케이션을 생성하고, 설정을 가져와 어플리케이션을 초기화 한다.
app = Flask(__name__)
app.config.from_object(__name__)

# from_object 대신에 from_envvar을 이용하여 환경변수를 읽어 올 수 있다.
# app.config.from_envvar('FLASKR_SETTINGS', silent=True)


# from werkzeug.contrib.fixers import ProxyFix
# app.wsgi_app = ProxyFix(app.wsgi_app)


#------------------------------------------------------------------------------------------

@app.route('/')
def index_page():
    return "This is puding server's index page : "+ flask.request.remote_addr

@app.route('/test')
def show_entries():
    cur = g.db.execute('select title, text from entries order by id desc')
    entries = [dict(title=row[0], text=row[1]) for row in cur.fetchall()]
    template_name = 'test.html'

    return render_template(template_name, entries=entries)


@app.route('/user/login')
def user_login():
    return "user_login"

@app.route('/user/logout')
def user_logout():
    return "user_logout"






#------------------------------------------------------------------------------------------
# db 접근이 쉬워지도록 도와주는 부분이다.
def connect_db():
    return sqlite3.connect(app.config['DATABASE'])

# 데이터베이스를 초기화 시키는 init_db 함수
def init_db():
    with closing(connect_db()) as db:
        with app.open_resource('/root/server/fuding_wewe/fuding_server/fuding_s/static/schema.sql') as f:
            db.cursor().executescript(f.read())
        db.commit()

# request 실행되기 전에 호출되는 함수
@app.before_request
def before_request():
    g.db = connect_db()

@app.teardown_request
def teardown_request(exception):
    g.db.close()



#------------------------------------------------------------------------------------------
# 단독서버로 실행되는 어플리케이션을 위한 서버 실행 코드 
# if __name__ == '__main__':
    # app.run()

# 가상 서버에서 돌릴 때에는 다음과 같이 run host를 지정한다.
# 지금 지정된 IP는 TOAST CLOUD 주소.
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)


# run on tornado server
# if __name__=="__main__": 
#     http_server = HTTPServer(WSGIContainer(app))
#     http_server.listen(5000)
#     IOLoop.instance().start()
    #app.run(host='0.0.0.0', port=8080)




