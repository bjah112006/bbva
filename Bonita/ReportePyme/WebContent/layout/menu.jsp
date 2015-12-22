<div id="menu">
    <div class="ui-widget-header ui-corner-all">
        <table class="panelMenu">
            <tr>
                <td>
                    <table class="tblMenuL">
                        <tr>
                            <td>
                                <div class="menuBar">
                                    <a class="fg-button fg-button-ltr ${requestScope.schedulerClass}" href="<%=request.getContextPath()%>/scheduler/index.html">
                                        <div class="title title-ico title-ico-calendario">Estado de procesos</div>
                                    </a>
                                </div>
                            </td>
                            <td>
                                <a class="fg-button ${requestScope.jobClass}" href="<%=request.getContextPath()%>/scheduler/configure.html">
                                    <div class="title title-ico title-ico-procesos">Configuraci&#243;n de trabajos</div>
                                </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</div>
