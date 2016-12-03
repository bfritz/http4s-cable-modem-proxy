// (c) 2005 Motorola, Inc. All Rights Reserved. 
// $Id: utility.js 2004/07/22 22:01:05 cdo 
// $Id: utility.js 2005/07/18 14:11:05 jchristensen: Added WanMgcpLogs page 
// HTML tags. 

var LabelBgColor = "#000000";HeaderBgColor = "#CCCC99";HeaderHgColor = "#666600";ItemHgColor = "#FFFFFF";ItemFgColor = "#485A91";SideBgColor = "#736B08";SideHgColor = "#000000";SideFgColor = "#FFFFFF";BodyBgColor = "#FFFFFF";TableHeaderBgColor = "#736b08";SideFrameBgColor = "#CCCC99";SideLogo = "logo.gif";CopyrightImage = "copyright.gif";

function sideBg(item, curSection)
{
return ( item == curSection ? SideHgColor :  SideBgColor );
}

function displaySideItem(item, curSection, ref, itemText)
{
var outputHtml="";

outputHtml = 
'  <TR>'+
'    <TH bgColor=' + sideBg(item,curSection) + '><A href=' + ref + ' style="TEXT-DECORATION: none" target=_top>'+
'      <FONT face="Arial, Helvetica" color=' + SideFgColor+'>' + itemText +'</FONT></A></TD>'+
'  </TR>';

return outputHtml;
}



function displayInfoSection(infoText)
{
var outputHtml;

outputHtml =
'<TABLE align=center border=0 cellPadding=5 cellSpacing=0 width="100%">' +
'<TBODY>' +
'  <TR>' +
'     <TD>' + 
	    infoText +
'     </TD>' +
'  </TR>' +
'</TBODY>' +
'</TABLE>' +
'<hr size="3" align="center" width="100%" color="#7b2939">'
+
'<P></P>';

return outputHtml;
}

function itemBg(current, item)
{
return ( current == item ? HeaderHgColor :  HeaderBgColor );
}

function itemFg(current, item)
{
return ( current == item ? ItemHgColor :  ItemFgColor );
}

function itemHeader(current, item, ref, itemText)
{
var outputHtml;

outputHtml =     
'<TH bgColor=' + itemBg(current,item) + '>' +
'<A href=' + ref + ' style="TEXT-DECORATION: none" target=_top>' +
'<FONT color=' + itemFg(current,item) + ' face=Arial,Helvetica>' + itemText + '</FONT></A></TH>';

return (outputHtml);

}

function displayCmHeader(name, infoText)
{
var outputHtml;

//cqvds00018753 - Open Source section added.
outputHtml = 
'<TABLE border=0 cellPadding=5 cellSpacing=0 width="100%">'+
  '<TBODY>'+
  '<TR>'+
	itemHeader(name, "cmStatus", "index.htm", "Status") + 
	itemHeader(name, "cmSignal", "cmSignal.htm", "Signal") + 
	itemHeader(name, "cmAddress", "cmAddress.htm", "Addresses") + 
	itemHeader(name, "cmConfig", "cmConfig.htm", "Configuration") + 
	itemHeader(name, "cmLogs", "cmLogs.htm", "Logs") + 
	itemHeader(name, "cmOpenSource", "cmOpenSource.htm", "Open Source") +
	itemHeader(name, "cmHelp", "cmHelp.htm", "Help") +
  '</TR>'+
  '</TBODY>'+
'</TABLE>';

return outputHtml;
}
function displaySectionHeader(section, name, infoText)
{

   if (section == "cm") 
   {
      return displayCmHeader(name, infoText);
   }

}

function displaySectionLabel(section)
{
   if (section == "cm")
      return "Cable Modem";
}


function displayHeader(section, name, infoText)
{

var outputHtml;

outputHtml =
'<TABLE border=0 cellPadding=5 cellSpacing=0 width="100%">'+ 
'<TBODY>'+
'<TR>'+
   '<TH>'+
   '<FONT color=white face=Arial,Helvetica>' + displaySectionLabel(section) + '</FONT>'+
   '</TH>'+
'</TR>'+
'</TBODY>'+
'</TABLE>';

outputHtml += displaySectionHeader(section, name, infoText);

// Skip this section if Help Page
if (name != "cmHelp")
{
outputHtml += displayInfoSection(infoText);
}

return outputHtml;
}



function displaySectionFooter(section)
{

var outputHtml;

//cqvds00018753 - Open Source section added.
if (section == "cm")
{
outputHtml = 
'<A href="index.htm" target=_top>Status</A> | \
<A href="cmSignal.htm" target=_top>Signal</A> | \
<A href="cmAddress.htm" target=_top>Addresses</A> | \
<A href="cmConfig.htm" target=_top>Configuration</A> | \
<A href="cmLogs.htm" target=_top>Logs</A> | \
<A href="cmOpenSource.htm" target=_top>Open Source</A> | \
<A href="cmHelp.htm" target=_top>Help</A>';
}

return outputHtml;
}



function displayFooter(section)
{

var outputHtml =
'<P></P> \
<hr size="3" align="center" width="100%" color="#7b2939">\
<P></P> \
\
<STRONG> \
<CENTER><FONT face="Arial, Helvetica"> \
<P></P> \
<P>'  +

displaySectionFooter(section) +

'</P> \
</FONT> \
</CENTER> \
</STRONG> \
\
<TABLE align=center border=0 cellSpacing=0> \
  <TBODY> \
  <TR> \
    <TD> \
      <CENTER><IMG src=' + CopyrightImage + ' width="132" height="26"></CENTER></TD></TR> \
  <TR> \
    <TD> \
      <CENTER><SMALL><FONT color=#000000 face="Arial , Helvetica"> \
	  <A href="legal.htm" target=_top>© Copyright 2012 - 2013 Motorola Mobility, LLC. All rights reserved.</A> </FONT> \
	  </SMALL></CENTER> \
	 </TD> \
  </TR> \
  </TBODY> \
</TABLE>';

return outputHtml;
	  
}

function displayLogo()
{
    var outputHtml = '\
    <A href="index.htm" target=_top> \
        <IMG align=center border=0 src=' + SideLogo + '> \
    </A>';

    return outputHtml;
}

function getcurrentfilename()
{
    var pathname = location.pathname;
    var filename = pathname.substr(pathname.lastIndexOf("\\")+2, pathname.length);
    return filename;
}

function onloadmainpage()
{
    document.body.style.backgroundColor = BodyBgColor; 

    var table = document.getElementsByTagName("TABLE");
    var filename = getcurrentfilename();
    if(filename == "legalData.htm" || filename == "reset.htm")
    {
        table[0].style.backgroundColor = TableHeaderBgColor;
    }
    else
    {
        table[0].style.backgroundColor = LabelBgColor;
    }

    for(var i=2; i<table.length; i++)
    {
        table[i].style.backgroundColor = BodyBgColor; 

        var tableHeaders = table[i].getElementsByTagName("TH");
        for(var j=0; j<tableHeaders.length; j=j+1)
        {
            tableHeaders[j].style.backgroundColor = TableHeaderBgColor;
        }
    }
}

function onloadsideframe()
{
    document.body.style.backgroundColor = SideFrameBgColor; 
}

function displayLogOutItem()
{
   var outputHtml =
      '  <TR><TD>&nbsp</TD></TR>' +
      '  <TR>'+
      '    <TH bgColor=' + SideBgColor + '><A href=' + "webLogout.htm" + ' style="TEXT-DECORATION: none" target=_top>'+
      '      <FONT face="Arial, Helvetica" color=' + SideFgColor+'>' + "Logout" +'</FONT></A></TD>'+
      '  </TR>';

   return outputHtml;
}

function getPwdEnableStatus()
{
   return "NO";
}

function displaySide(curSection)
{
   var pwdEnableStatus;
   var outputHtml;
   pwdEnableStatus = getPwdEnableStatus();

   if(pwdEnableStatus == "YES")
   {
      outputHtml = '<TABLE align=center border=1 borderColor=#cccc99 cellPadding=8 cellSpacing=0 width="100%">'+
         '  <TBODY>'+
         displayLogOutItem() +
         '  </TBODY>'+
         '</TABLE>';
   }
   else
   {
      outputHtml = '';
   }

   return outputHtml;
}

function displayLogoInLoginData()
{
    var outputHtml = '\
    <A href="index.htm" target=_top> \
        <IMG align=left border=0 src=' + SideLogo + '> \
    </A>';

    return outputHtml;
}

function displayLogoInLogout()
{
    var outputHtml = '\
    <A href="index.htm" target=_top> \
        <IMG border=0 src=' + SideLogo + '> \
    </A>';

    return outputHtml;
}

