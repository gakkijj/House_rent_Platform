<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/head.jsp"%>

<section class="gray-simple">

    <div class="container">


        <div class="row">

            <div class="col-lg-12 col-md-12">
                <div class="property_block_wrap style-2">

                    <div id="content" style="padding: 10px">
                        <h2 style="text-align:center">房屋租赁合同</h2>
                        <p>     出租方：<strong>${order.ownerUser.userDisplayName}</strong>（以下简称甲方）</p>
                        <p>     身份证：<strong>${order.ownerUser.idCard}</strong></p>
                        <p>     承租方：<strong>${order.customerUser.userDisplayName}</strong>（以下简称甲方）</p>
                        <p>     身份证：<strong>${order.customerUser.idCard}</strong>（以下简称甲方）</p>
                        <p>     根据甲、乙双方在自愿、平等、互利的基础上，经协商一致，为明确双方之间的权利义务关系，就甲方将其合法拥有的房屋出租给乙方使用，乙方承租甲方房屋事宜，订立本合同。</p>
                        <p>     一、房屋地址：<strong>${order.house.address}</strong>，房屋名称：<strong>${order.house.title}</strong>.</p>
                        <p>     二、租赁期及约定</p>
                        <p>     1、该房屋租赁期共<strong>${order.dayNum}</strong>天。自<strong>
                            <fmt:formatDate pattern="yyyy-MM-dd" value="${order.startDate}"/>
                        </strong> 到
                            <strong> <fmt:formatDate pattern="yyyy-MM-dd" value="${order.endDate}"/></strong>。
                        </p>
                        <p>     2、房屋租金：每日 <strong>
                            <fmt:formatNumber value="${order.monthRent/30}" pattern="#" type="number"/></strong> 元，时长
                            <strong>${order.dayNum}</strong> 天，押金 <strong>0</strong> 元，共计
                            <strong>${order.totalAmount}</strong>元。
                        </p>
                        <p>     房屋终止，甲方验收无误后，将押金退还乙方，不计利息。</p>
                        <p>     3、乙方向甲方承诺，租赁该房屋仅作为普通住房使用。</p>
                        <p>     4、租赁期满，甲方有权收回出租房屋，乙方应如期交还。乙方如要求续租，则必须在租赁期满前一个月内通知甲方，经甲方同意后，重新签订租赁合同。</p>
                        <p>     三、违约处理：</p>
                        <p>     甲方违约：</p>
                        <p>     1、如甲方未能及时将押金退还乙方，甲方每天应按押金的2％向乙方支付违约金；</p>
                        <p>     2、如甲方提供的维修服务项目与协议不相符，乙方有权提前退租，甲方应全额退还剩余的租金。</p>
                        <p>     乙方违约：</p>
                        <p>     1、如乙方不按时缴纳租金，甲方有权在其押金中扣除，同时收回房屋，所有损失由乙方负责；</p>
                        <p>     2、如果乙方在租赁期限未满时退租，甲方仅退还剩余租金的40％（租期未满一个月的不在此例）；</p>
                        <p>     四、本协议书一式两份，甲乙双方各执一份；本协议甲乙双方签字后生效。补充协议亦属于合同的一部分。</p>
                        <p>     甲方签字：_______________________乙方签字：_______________________</p>
                        <p>     联系方式：_______________________联系方式：_______________________</p>
                    </div>
                    <div style="text-align: center;margin-top: 50px;">
                        <%--        订单状态：-3租客已取消 -2待签合同 -1待收款 0生效中 1已到期 2退租申请 3退租申请不通过--%>
                        <c:choose>
                            <c:when test="${order.status==-2}">
                                <a href="javascript:void(0)" onclick="confirmAgreement(${order.id})"
                                   class="btn btn-primary text-dark">我已阅读并同意上述合同</a>
                            </c:when>
                            <c:when test="${order.status==-1}"><a href="/order/pay?orderId=${order.id}">去付款</a></c:when>
                            <c:when test="${order.status==0}">合同已生效</c:when>
                            <c:when test="${order.status==1}">合同已到期</c:when>
                            <c:when test="${order.status==2}">已申请退租</c:when>
                            <c:when test="${order.status==3}">退租申请驳回</c:when>
                            <c:when test="${order.status==-3}">已取消</c:when>
                        </c:choose>
                    </div>
                    <div style="text-align: center">
                        <a href="javascript:void(0)" onclick="printHtml('content')">打印</a>
                    </div>


                </div>
            </div>
        </div>

    </div>
</section>

<%@include file="../common/footer.jsp"%>
<script>
    function printHtml(div) {

        var before = "<html><head><meta http-equiv='content-type' content='text/html; charset=utf-8'></head><body>";
        var print = document.getElementById(div).innerHTML;
        var result = before+print+"</body></html>"

        console.log(result);
        var wind = window.open("", 'newwindow', 'height=300, width=700, top=100, left=100, toolbar=no, menubar=no, scrollbars=no, resizable=np, location=no, status=no');
        wind.document.body.innerHTML = result;
        wind.print();
        return false;

    }
</script>

