<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <script type="text/javascript" src="${pageContext.request.contextPath}/view/scheduler/js/configure.js"></script>
        <div id="pnlTareas" class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">Programaci&oacute;n</label>
            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <table>
                    <tr>
                        <td>Hora de ejecución:&nbsp;</td>
                        <td>
                            <input type="text" class="ui-text ui-text-time" id="txtHora" maxlength="5"/><span class="ui-text-time-icon" onclick="$('#txtHora').focus();"></span>
                        </td>
                        <td>&nbsp;&nbsp;<button id="btnProgramar">Programar</button></td>
                    </tr>
                </table>
            </div>
        </div>
        
        <div id="pnlTareas" class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">Ejecuci&oacute;n Manual</label>
            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <table>
                    <tr>
                        <td>Fecha a procesar:&nbsp;</td>
                        <td>
                            <input type="text" class="ui-text ui-text-date" id="txtFecha" maxlength="10"/><span class="ui-text-date-icon" onclick="$('#txtFecha').focus();"></span>
                        </td>
                        <td>&nbsp;&nbsp;<button id="btnEjecutar">Ejecutar</button></td>
                    </tr>
                </table>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>