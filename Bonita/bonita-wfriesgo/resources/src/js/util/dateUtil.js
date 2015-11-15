'use strict';

bonitaApp.factory('DateUtil', function() {
     
    var factory = {
        yyyymmdd: 'yyyyMMdd',
        DDMMYYYY: 'dd/MM/yyyy',
        DDMMYYYYHHmmss: 'dd/MM/yyyy HH:mm:ss',
        MMDDYYYY: 'MM/dd/yyyy',
        YYYYMMDD: 'yyyy/dd/MM',
        parse: function(val, format) {
            var dd = 1, mm = 1, yy = 1900;
        
            if(format == this.DDMMYYYY) {
                dd = parseInt(val.substring(0,2), 10);
                mm = parseInt(val.substring(3,5), 10);
                yy = parseInt(val.substring(6,10), 10);
            } else if(format == this.MMDDYYYY) {
                mm = parseInt(val.substring(0,2), 10);
                dd = parseInt(val.substring(3,5), 10);
                yy = parseInt(val.substring(6,10), 10);
            } else if(format == this.YYYYMMDD) {
                yy = parseInt(val.substring(0,4), 10);
                mm = parseInt(val.substring(5,7), 10);
                dd = parseInt(val.substring(8,10), 10);
            } else if(format == this.yyyymmdd) {
                yy = parseInt(val.substring(0,4), 10);
                mm = parseInt(val.substring(4,6), 10);
                dd = parseInt(val.substring(6,8), 10);
            }
        
            return new Date(yy, mm - 1, dd, 0, 0, 0);
        },
        compareTo: function(val1, val2, format) {
            return (this.parse(val1, format).getTime() > this.parse(val2, format).getTime());
        },
        addDays: function(date, numDays) {
            return new Date(date.getTime() + (numDays * 24 * 3600 * 1000));
        },
        lastDay: function(val, format) {
            var date = this.parse(val, format);
        
            d = 1;
            m = date.getMonth() + 1;
            y = (m == 12 ? 1 : 0) + date.getFullYear();
            m = (m == 12 ? 0 : m);
        
            date = new Date(y, m, d, 0, 0, 0);
        
            return this.addDays(date, -1);
        },
        toString: function(date, format) {
            var dd = '01', mm = '01', yy = '1900', hh = "00", nn = "00", ss = "", val = "01/01/1900";
        
            if(format == this.DDMMYYYY) {
                dd = date.getDate();
                mm = date.getMonth() + 1;
                yy = date.getFullYear();
            
                val  = (dd < 10 ? '0' : '') + dd + "/";
                val += (mm < 10 ? '0' : '') + mm + "/";
                val += yy;
            } else if(format == this.DDMMYYYYHHmmss) {
                dd = date.getDate();
                mm = date.getMonth() + 1;
                yy = date.getFullYear();
                hh = date.getHours();
                nn = date.getMinutes();
                ss = date.getSeconds();
            
                val  = (dd < 10 ? '0' : '') + dd + "/";
                val += (mm < 10 ? '0' : '') + mm + "/";
                val += yy;
                val += " ";
                val += (hh < 10 ? '0' : '') + hh + ":";
                val += (nn < 10 ? '0' : '') + nn + ":";
                val += (ss < 10 ? '0' : '') + ss;
            }
        
            return val;
        },
        longToDate: function(milliseconds) {
            return new Date(milliseconds);
        }
    };
 
    return factory;
});
