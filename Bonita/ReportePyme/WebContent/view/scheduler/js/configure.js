// http://127.0.0.1:18080/WFFastPymeBBVA/scheduler/rescheduler/migrarDocumentosJob.json

$(document).ready(function(){
    $("#txtFecha").datepicker({dateFormat: "dd/mm/yy"});
    $('#txtHora').timepicker({       
        timeFormat: 'HH:mm'
    });
    
    $("#btnProgramar").button({icons: {primary: 'ui-icon-disk'}}).on("click", function(){
        var parts = $("#txtHora").val().split(':');
        var param = {"cron": "0 " + parts[1] + " " + parts[0] + " 1/1 * ? *"};
        
        _post = $.post(obtenerContexto("scheduler/rescheduler/migrarDocumentosJob.json"), param);
        _post.success(function(data) {
            /*
            {
              "rescheduler": "OK",
              "message": "Job started"
            }
             */
            if(data.rescheduler == "OK") {
                openJqInfo({
                    content: data.message
                });
                
            } else {
                openJqError({type: "SYS", content: data.message});
            }
        });
    });
    
    $("#btnEjecutar").button({icons: {primary: 'ui-icon-disk'}}).on("click", function(){
        var param = {"date": $("#txtFecha").val()};
        
        _post = $.post(obtenerContexto("scheduler/execute/migrarDocumentosJob.json"), param);
        _post.success(function(data) {
            /*
            {
              "execute": "OK",
              "message": "Job started"
            }
             */
            if(data.execute == "OK") {
                openJqInfo({
                    content: data.message
                });
                
            } else {
                openJqError({type: "SYS", content: data.message});
            }
        });
    });
});