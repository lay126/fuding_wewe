# -*- coding: UTF-8 -*-
# 한글 사용을 위한 인코딩 설정 

# 데이터베이스 사용을 위한 부분
from __future__ import with_statement
from contextlib import closing

# all the imports
import sqlite3
from flask import Flask, request, session, g, redirect, url_for, abort, render_template, flash
from jinja2 import Template

# for image file
import os
from flask import Flask, request, redirect, url_for
from werkzeug import secure_filename

from flask_peewee.auth import Auth
from flask_peewee.db import Database


#------------------------------------------------------------------------------------------
# configuration
DATABASE = '/tmp/flaskr.db'
DEBUG = True
SECRET_KEY = 'development key'
USERNAME = 'admin'
PASSWORD = 'default'

#------------------------------------------------------------------------------------------
# create our little application :)
# 실제 어플리케이션을 생성하고, 설정을 가져와 어플리케이션을 초기화
app = Flask(__name__)
app.config.from_object(__name__)


#------------------------------------------------------------------------------------------
# image file
UPLOAD_FOLDER = '/Users/ayoung/git/fuding_wewe/fuding_server/fuding_s/images/uploads'
# UPLOAD_FOLDER = '/root/server/fuding_wewe/fuding_server/fuding_s/images/uploads'
ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER




# DB #
#------------------------------------------------------------------------------------------
# db 접근이 쉬워지도록 도와주는 부분
def connect_db():
    return sqlite3.connect(app.config['DATABASE'])

# 데이터베이스를 초기화 시키는 init_db 함수
def init_db():
    with closing(connect_db()) as db:
        with app.open_resource('/Users/ayoung/git/fuding_wewe/fuding_server/fuding_s/static/schema.sql') as f:
        # with app.open_resource('/root/server/fuding_wewe/fuding_server/fuding_s/static/schema.sql') as f:
            db.cursor().executescript(f.read())
        db.commit()




#------------------------------------------------------------------------------------------
@app.route('/')
def index_page():
    return "This is puding server's index page : " + flask.request.remote_addr

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
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/aa', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        file = request.files['file']
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
            return redirect(url_for('uploaded_file', filename=filename))
    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>Upload new File</h1>
    <form action="" method=post enctype=multipart/form-data>
      <p><input type=file name=file>
         <input type=submit value=Upload>
    </form>
    '''







#------------------------------------------------------------------------------------------
# 단독서버로 실행되는 어플리케이션을 위한 서버 실행 코드 
# if __name__ == '__main__':
    # app.run()

# 가상 서버에서 돌릴 때에는 다음과 같이 run host를 지정
# 지금 지정된 IP는 TOAST CLOUD 주소.
if __name__ == '__main__':
    app.run()
    # app.run(host='0.0.0.0', port=9006)

