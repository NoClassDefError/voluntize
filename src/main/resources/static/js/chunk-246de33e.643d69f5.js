(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-246de33e"],{"09b5":function(e,t,a){},"19fd":function(e,t,a){"use strict";var n=a("09b5"),r=a.n(n);r.a},"8e15":function(e,t,a){"use strict";var n=a("2b0e");new n["default"]},a3ee:function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-breadcrumb",{attrs:{separator:"/"}},[a("el-breadcrumb-item",[e._v("系统管理")]),a("el-breadcrumb-item",[e._v("审核")])],1),a("el-card",[a("div",[a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.exaArray()}}},[e._v("批量审核")]),a("el-button",{attrs:{type:"danger"},on:{click:function(t){return e.toggleSelection()}}},[e._v("取消选择")])],1),a("el-divider"),a("el-table",{ref:"multipleTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,border:"","tooltip-effect":"dark","max-height":"1000px"},on:{"selection-change":e.handleSelectionChange}},[a("el-table-column",{attrs:{type:"selection",width:"55"}}),a("el-table-column",{attrs:{prop:"name",label:"姓名",width:"300"}}),a("el-table-column",{attrs:{prop:"number",label:"学号",width:"400"}}),a("el-table-column",{attrs:{prop:"sex",label:"性别",width:"150"}}),a("el-table-column",{attrs:{prop:"acdemic",label:"学院",width:"400"}}),a("el-table-column",{attrs:{prop:"status",label:"状态",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-switch",{on:{change:function(a){return e.userstatechange(t.row)}},model:{value:t.row.status,callback:function(a){e.$set(t.row,"status",a)},expression:"scope.row.status"}})]}}])})],1)],1)],1)},r=[],l=(a("96cf"),a("1da1")),c=(a("8e15"),{name:"examnine",data:function(){return{multipleSelection:[],exaarr:[],tableData:[{name:"张三",number:"123456",sex:"男",acdemic:"控计",status:!1},{name:"张三",number:"12",sex:"男",acdemic:"控计",status:!1},{name:"张三",number:"12345",sex:"男",acdemic:"控计",status:!1}]}},methods:{toggleSelection:function(){this.$refs.multipleTable.clearSelection()},userstatechange:function(e){return console.log(e),this.$message({message:"更新成功",type:"success"})},gettabledata:function(){},handleSelectionChange:function(e){this.multipleSelection=e},exaArray:function(){var e=Object(l["a"])(regeneratorRuntime.mark((function e(){var t,a;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:for(this.exaarr=[],t=0;t<this.multipleSelection.length;t++)this.exaarr[t]=this.multipleSelection[t].number;return e.next=4,this.$http.post("department/service/approve",this.exaarr,{headers:{"Content-Type":"application/x-www-form-urlencoded"}});case 4:a=e.sent,a.data;case 6:case"end":return e.stop()}}),e,this)})));function t(){return e.apply(this,arguments)}return t}()}}),i=c,s=(a("19fd"),a("2877")),o=Object(s["a"])(i,n,r,!1,null,"4d171c86",null);t["default"]=o.exports}}]);
//# sourceMappingURL=chunk-246de33e.643d69f5.js.map