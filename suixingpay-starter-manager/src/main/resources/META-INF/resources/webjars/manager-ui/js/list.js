//Vue对象上下文，当中的属性可以任意获取，例如 app.$data.items
var app = new Vue({
    el: '#app',
    data: {
        items: [],
        fields: [
            {
                key: 'title',
                label: '名称'
            }
        ]
    }

});

function handleError(error){
    if (error.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          alert("http状态码：" + error.response.status + "，错误消息:"+error.response.data);
          console.log(error.response.headers);
    } else if (error.request) {
          // The request was made but no response was received
          // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
          // http.ClientRequest in node.js
          console.log(error.request);
    } else {
          // Something happened in setting up the request that triggered an Error
          console.log('Error', error.message);
    }
    console.log(error.config);
}

/**
 * 加载数据
 */
function loadData() {
    axios.get("manager-ui/")
      .then(function (response) {
        console.log(response);
        app.$data.items = response.data;
      })
      .catch(function (error) {
        handleError(error);
      });
}

/**
 * 访问管理器url
 * @param item
 * @param index
 * @param event
 */
function access(item,index,event) {
    window.open(item.url);
}

loadData();