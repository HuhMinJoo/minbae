var page = 0;
var user_lat = JSON.parse(window.localStorage.getItem("user_latlng")).user_lat;
var user_lng = JSON.parse(window.localStorage.getItem("user_latlng")).user_lng;
var resultdata;

$('#body').on('load', requestStandardStoreList());

window.onscroll = function (e) {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
        requestStandardStoreList();
    }
}

function requestStandardStoreList() {
    if (location.href.startsWith("http://huhming.ga/user/category/click/")) {
        $.ajax({
            type: 'GET',
            url: '/user/category/standard?paging=' + page + '&user_lat=' + user_lat + '&user_lng=' + user_lng + '&category=' + (window.location.href.split('/', 7)[6]),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
        }).done(function (data) {
            if (data != null && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var a = document.createElement("a");
                    a.href = "/user/store/detail/" + (window.location.href.split('/', 7)[6]) + "/" + data[i].store_idx + "/star";
                    a.className = "link-secondary";
                    a.style.textDecorationLine='none !important';
                    var div = document.createElement("div");
                    div.className = "card mb-3";
                    div.style.maxWidth = "750px";
                    var div1 = document.createElement("div");
                    div1.className = "row g-0";
                    var div2 = document.createElement("div");
                    div2.className = "col-md-4";
                    var img = document.createElement("img");
                    img.src = "/user/image/" + data[i].store_detail_info_img;
                    img.className = "img-fluid rounded-start";
                    img.style.width = "15rem";
                    img.style.height = "9rem";
                    div2.append(img);
                    var div3 = document.createElement("div");
                    div3.className = "col-md-8";
                    var div4 = document.createElement("div");
                    div4.className = "card-body";
                    var h5 = document.createElement("h5");
                    h5.className = "card-title";
                    h5.innerText = data[i].store_name;
                    var p1 = document.createElement("p1");
                    p1.className = "card-text";
                    if (data[i].avger_star != undefined) {
                        p1.innerHTML = data[i].avger_star.toFixed(1) + "점<br/>";
                    } else {
                        p1.innerHTML = "0점" + "<br/>";
                    }
                    var p2 = document.createElement("p2");
                    p2.className = "card-text";
                    var small = document.createElement("small");
                    small.className = "text-muted";
                    small.innerText = "최소 주문 " + data[i].store_detail_minimum_price;
                    p2.append(small);
                    a.append(div);
                    div.append(div1);
                    div1.append(div2);
                    div1.append(div3);
                    div3.append(div4);
                    div4.append(h5);
                    div4.append(p1);
                    div4.append(p2);
                    $('#storeListDiv').append(a);
                }
            }
            resultdata = data;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
        page++;
    }
}