var now = new Date();
mydate = new Date();
myday = mydate.getDay();
mymonth = mydate.getMonth();
myweekday= mydate.getDate();
weekday= myweekday;
myyear= mydate.getYear();
year = myyear;
if (myweekday<10)
    myweekday = "0"+myweekday.toString();
if(myday == 0) day = " Domingo, "
else if(myday == 1)day = " Segunda-Feira"
else if(myday == 2) day = " Terça-Feira"
else if(myday == 3) day = " Quarta-Feira"
else if(myday == 4)day = " Quinta-Feira"
else if(myday == 5) day = " Sexta-Feira"
else if(myday == 6) day = " Sábado"
if(mymonth == 0)
    month = "/01/"
else if(mymonth ==1) month = "/02/"
else if(mymonth ==2) month = "/03/"
else if(mymonth ==3) month = "/04/"
else if(mymonth ==4) month = "/05/"
else if(mymonth ==5) month = "/06/"
else if(mymonth ==6) month = "/07/"
else if(mymonth ==7) month = "/08/"
else if(mymonth ==8 ) month = "/09/"
else if(mymonth ==9) month = "/10/"
else if(mymonth ==10) month = "/11/"
else if(mymonth ==11) month = "/12/"
if (navigator.appName.indexOf('Microsoft') != -1)
    document.write(myweekday+ month + year + ","+ day);
else
{
    year=year+1900;
    document.write(myweekday+ month + year + ","+ day);
}
