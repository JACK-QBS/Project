// var $http = axios.create({
//     // headers: {
//     //     'content-type': 'application/x-www-form-urlencoded'
//     //     'content-type': 'application/json'
//     // }
// });


const errorHandler = (err, vm, info)=>{
    if(err){
        if(err.data && err.callback){
            err.callback();
            return;
        }
        console.error("=======================catch error=======================");
        console.error(err);
        // console.debug(vm);
        // console.debug(info);
        const h = window.vm.$createElement;
        window.vm.$msgbox({
            title: '前端代码报错啦',
            message: h('div', { style: 'color: teal' }, [
                h('p', null, '前端代码捕获到异常啦'),
                h('p', null, '[提示] 打开开发者工具，看看Console面板中有没有前端代码报错'),
                h('p', null, '[Plus] 如果有ajax请求，返回的数据中data字段格式不对，前端代码解析也可能导致前端报错哦'),
            ]),
            confirmButtonText: '确定',
            customClass: 'err'
        });
    }
}

Vue.config.errorHandler = errorHandler;
Vue.prototype.$throw = err => errorHandler(err, this);

// http request 拦截器
axios.interceptors.request.use(
    config => {
        // const token = sessionStorage.getItem('token')
        // if (token ) { // 判断是否存在token，如果存在的话，则每个http header都加上token
        //     config.headers.authorization = token  //请求头加上token
        // }
        return config;
    },
    err => {
        console.error("=======================request error=======================");
        console.debug(JSON.stringify(err, null, "\t"));
        return Promise.reject(err)
    }
);

function responseError(err, ok){
    // console.error("=======================response error=======================");
    // console.debug(JSON.stringify(err));
    // console.debug(JSON.stringify(err, null, "\t"));
    if(ok){
        return {
            data: err,
            callback: function () {
                const h = window.vm.$createElement;
                let n = [];
                n.push(h('el-row', { class: "err-row" }, [
                    h('el-tag', { attrs: {effect: "dark", type: "success"}, class: "err-tag" }, [
                        err.config.method.toUpperCase()+' '+err.config.url
                    ]),
                    h('el-tag', { attrs: {effect: "dark", type: "success"}, class: "err-tag" }, [
                        err.status
                    ]),
                ]));

                if(err.data.code){
                    n.push(h('el-row', { class: "err-row" }, [
                        h('p', { style: 'color: red' }, '错误码：'+err.data.code+(err.data.message ? '，错误信息：'+err.data.message : '')),
                    ]));
                }
                n.push(h('el-row', { class: "err-row" }, [
                    h('p', null, '响应的success不为true，所以报错啦'),
                ]));
                n.push(h('el-row', { class: "err-row" }, [
                    h('p', null, '[提示] 先抓包看看响应的内容，根据响应定位问题。如果后台有异常，也记得要检查 tomcat 输出的异常堆栈信息'),
                ]));
                window.vm.$msgbox({
                    title: '请求发生错误啦',
                    message: h('div', { style: 'color: teal' }, n),
                    confirmButtonText: '确定',
                    customClass: 'err'
                });
            },
        }
    }else{
        return {
            data: err,
            callback(){
                const h = window.vm.$createElement;
                let n = [];
                let n1 = [];
                n1.push(h('el-tag', { attrs: {effect: "dark", type: "danger"}, class: "err-tag" }, [
                    err.config.method.toUpperCase()+' '+err.config.url
                ]));
                if(err.response){
                    n1.push(h('el-tag', { attrs: {effect: "dark", type: "danger"}, class: "err-tag" }, [
                        err.response.status
                    ]));
                }
                n.push(h('el-row', { class: "err-row" }, n1));

                if(!err.config.url.startsWith("http://") && !err.config.url.startsWith("https://")){
                    n.push(h('el-row', { class: "err-row" }, [
                        h('p', null, '[注意] 这里的url是相对路径，可以抓包查看请求的绝对路径哦'),
                    ]));
                }
                if(!err.response){
                    n.push(h('el-row', { class: "err-row" }, [
                        h('p', null, '没有接收到服务器响应'),
                    ]));
                    n.push(h('el-row', { class: "err-row" }, [
                        h('p', null, '[原因] IP无法ping通，或是端口号对应的应用程序无法访问（如没有启动tomcat，或启动出错）'),
                    ]));
                    n.push(h('el-row', { class: "err-row" }, [
                        h('p', null, '[提示] 先抓包看看请求的IP和端口号，在ping IP或是netstat -ano | findstr 端口号'),
                    ]));
                }else{
                    let tip = '[提示] ';
                    switch (err.response.status) {
                        case 400: {
                            tip += '400 Bad Request: 客户端请求的语法错误，服务器无法理解（类型转换错误，格式校验失败等等）';
                            break;
                        }
                        case 401: {
                            tip += '401 Unauthorized: 没有登录不允许访问哦';
                            break;
                        }
                        case 403: {
                            tip += '403 Forbidden: 没有权限, 禁止访问哦';
                            break;
                        }
                        case 404: {
                            tip += '404 Not Found: 找不到资源哦';
                            break;
                        }
                        case 405: {
                            tip += '405 Method Not Allowed: http请求方法不允许访问这个路径';
                            break;
                        }
                        case 500: {
                            tip += '500 Internal Server Error: 服务器出现错误了哦';
                            break;
                        }
                        default: {
                            tip += err.response.status;
                            tip += ': 查询下这个状态码代表的意思';
                        }
                    }
                    n.push(h('el-row', { class: "err-row" }, [
                        h('p', null, tip),
                    ]));
                }

                window.vm.$msgbox({
                    title: '请求发生错误啦',
                    message: h('div', { style: 'color: teal' }, n),
                    confirmButtonText: '确定',
                    customClass: 'err'
                });
            }
        }
    }
}

// http response 拦截器
axios.interceptors.response.use(
    response => {
        // console.debug("=======================response=======================");
        // console.debug(JSON.stringify(response));
        // console.debug(JSON.stringify(response, null, "\t"));
        //拦截响应，做统一处理
        // console.log(response.data)
        if (response.data.success || response.headers['content-type'] !== 'application/json') {
            return Promise.resolve(response.data);
        }else {
            return Promise.reject(responseError(response, true));
        }
        // return response.data;
    },
    //非200状态码时的错误处理
    err => {
        return Promise.reject(responseError(err)) // 返回接口返回的错误信息
    }
);

function copy(o) {
    return JSON.parse(JSON.stringify(o));
}

function showSuccess(vm, title, callback) {
    vm.$message({
        message: title,
        type: 'success',
        duration: 1000,
        onClose: function () {
            !callback || callback();
        },
    });
}

function ajaxError(vm, e, custom) {
    if(e && e.data && e.data.data && e.data.data.message) {
        if(custom) return e.data.data.message;
        vm.$message({
            message: e.data.data.message,
            type: 'error',
            duration: 1000,
        });
    }else{
        vm.$throw(e);
    }
}