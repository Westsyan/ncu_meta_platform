/**
 * Create by xwq
 * 2020-04-03
 * @param：用postMessage实现ifrme框内的刷新跳转事件,实现此功能还需引入jquery.cookie.js
 */
function jumpConfig(plat) {
    let localHref = window.location.href;
    let host = window.location.hostname;
    let parentHref = 'http://' + host + '/project/' + plat;
    window.parent.postMessage(localHref,parentHref);

    /**
     *当新标签页打开时，实现还是主页面的效果
     *如果报错，则证明是在平台内，如果没有报错，则是在子页面
     */
    try {
        let parent = window.parent.location.href
        if (parent === localHref) {
            if (window.name === "") {
                let uuid2 = random4() + random4();
                window.name = uuid2;
                let cookie1 = plat + uuid2;
                //添加path，主页面才能获取子页面的cookie
                $.cookie(cookie1, localHref, { path: '/' })
            }
            window.location.href=(parentHref)
        }
    } catch (e) {

    }
}

function random4() {
    return ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
}

