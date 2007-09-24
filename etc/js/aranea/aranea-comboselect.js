/************************************************************************************************************
Editable select
Copyright (C) September 2005  DTHMLGoodies.com, Alf Magne Kalleland

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

Dhtmlgoodies.com., hereby disclaims all copyright interest in this script
written by Alf Magne Kalleland.

Alf Magne Kalleland, 2006
Owner of DHTMLgoodies.com

************************************************************************************************************/

var arrowImage = function() {
  return araneaPage().getServletURL() + '/fileimporter/gfx/comboselect/select_arrow.gif';	// Regular arrow
}
var arrowImageOver = function() {
  return araneaPage().getServletURL() + '/fileimporter/gfx/comboselect/select_arrow_over.gif';	// Mouse over
}
var arrowImageDown = function() {
  return araneaPage().getServletURL() + '/fileimporter/gfx/comboselect/select_arrow_down.gif';	// Mouse down
}


var selectBoxIds = 0;
var currentlyOpenedOptionBox = false;
var editableSelect_activeArrow = false;



function selectBox_switchImageUrl()
{
	if(this.src.indexOf(arrowImage())>=0){
		this.src = this.src.replace(arrowImage(),arrowImageOver());
	}else{
		this.src = this.src.replace(arrowImageOver(),arrowImage());
	}


}

function selectBox_showOptions()
{
	if(editableSelect_activeArrow && editableSelect_activeArrow!=this){
		editableSelect_activeArrow.src = arrowImage();
	}
	editableSelect_activeArrow = this;

	var numId = this.id.replace(/[^\d]/g,'');
	var optionDiv = document.getElementById('selectBoxOptions' + numId);
	if(optionDiv.style.display=='block'){
		optionDiv.style.display='none';

		if (document.all && !window.opera) {
			showElement('SELECT',optionDiv);
			showElement('APPLET',optionDiv);
		}
		/*if(navigator.userAgent.indexOf('MSIE')>=0)document.getElementById('selectBoxIframe' + numId).style.display='none';*/

		this.src = arrowImageOver();
	}else{
		optionDiv.style.display='block';

		if (document.all && !window.opera) {
			hideElement('SELECT',optionDiv);
			hideElement('APPLET',optionDiv);
		}
		/*if(navigator.userAgent.indexOf('MSIE')>=0)document.getElementById('selectBoxIframe' + numId).style.display='block';*/

		this.src = arrowImageDown();
		if(currentlyOpenedOptionBox && currentlyOpenedOptionBox!=optionDiv)currentlyOpenedOptionBox.style.display='none';
		currentlyOpenedOptionBox= optionDiv;
	}
}

function selectOptionValue()
{
	var parentNode = this.parentNode.parentNode;
	var textInput = parentNode.getElementsByTagName('INPUT')[0];
	textInput.value = this.innerHTML;
	this.parentNode.style.display='none';
	document.getElementById('arrowSelectBox' + parentNode.id.replace(/[^\d]/g,'')).src = arrowImageOver();

	if (document.all && !window.opera) {
		showElement('SELECT',parentNode);
		showElement('APPLET',parentNode);
	}
	/*if(navigator.userAgent.indexOf('MSIE')>=0)document.getElementById('selectBoxIframe' + parentNode.id.replace(/[^\d]/g,'')).style.display='none';*/

}

var activeOption;
function highlightSelectBoxOption()
{
	if(this.style.backgroundColor=='#0a246a'){
		this.style.backgroundColor='';
		this.style.color='';
	}else{
		this.style.backgroundColor='#0a246a';
		this.style.color='#fff';
	}

	if(activeOption){
		activeOption.style.backgroundColor='';
		activeOption.style.color='';
	}
	activeOption = this;

}

function createEditableSelect(dest)
{
	dest.className='selectBoxInput';
	var div = document.createElement('DIV');
	div.style.styleFloat = 'left';
	div.style.width = dest.offsetWidth + 16 + 'px';
	div.style.position = 'relative';
	div.id = 'selectBox' + selectBoxIds;
	var parent = dest.parentNode;
	parent.insertBefore(div,dest);
	div.appendChild(dest);
	div.className='selectBox';
	div.style.zIndex = 10000 - selectBoxIds;

	var img = document.createElement('IMG');
	img.src = arrowImage();
	img.className = 'selectBoxArrow';

	img.onmouseover = selectBox_switchImageUrl;
	img.onmouseout = selectBox_switchImageUrl;
	img.onclick = selectBox_showOptions;
	img.id = 'arrowSelectBox' + selectBoxIds;

	div.appendChild(img);

	var optionDiv = document.createElement('DIV');
	optionDiv.id = 'selectBoxOptions' + selectBoxIds;
	optionDiv.className='selectBoxOptionContainer';
	optionDiv.style.width = div.offsetWidth + 'px'; // -2
	div.appendChild(optionDiv);

/*
	if(navigator.userAgent.indexOf('MSIE')>=0){
		var iframe = document.createElement('<IFRAME src="about:blank" frameborder=0>');
		iframe.style.width = optionDiv.style.width;
		iframe.style.height = optionDiv.offsetHeight + 'px';
		iframe.style.display='none';
		iframe.id = 'selectBoxIframe' + selectBoxIds;
		div.appendChild(iframe);
	}
*/

	if(dest.getAttribute('selectBoxOptions')){
		var options = dest.getAttribute('selectBoxOptions').split(';');
		var optionsTotalHeight = 0;
		var optionArray = new Array();
		for(var no=0;no<options.length;no++){
			var anOption = document.createElement('DIV');
			anOption.innerHTML = options[no];
			anOption.className='selectBoxAnOption';
			anOption.onclick = selectOptionValue;
			anOption.style.width = optionDiv.style.width.replace('px','') + 'px'; // -2
			anOption.onmouseover = highlightSelectBoxOption;
			optionDiv.appendChild(anOption);
			optionsTotalHeight = optionsTotalHeight + anOption.offsetHeight;
			optionArray.push(anOption);
		}
		if(optionsTotalHeight > optionDiv.offsetHeight){
			for(var no=0;no<optionArray.length;no++){
				optionArray[no].style.width = optionDiv.style.width.replace('px','') - 18 + 'px';
			}
		}
		optionDiv.style.display='none';
		optionDiv.style.visibility='visible';
	}

	selectBoxIds = selectBoxIds + 1;
}

/* =hide stuff for IE
-------------------------------------------------- */
function hideElement(elmID,overDiv) {
	if (!document.all) return;
	for (i=0; i<document.all.tags(elmID).length; i++) {
		var obj = document.all.tags(elmID)[i];
		if (!obj || !obj.offsetParent) continue;

		// Find the element's offsetTop and offsetLeft relative to the BODY tag.
		var objLeft = obj.offsetLeft;
		var objTop = obj.offsetTop;
		var objParent = obj.offsetParent;

		while ( typeof(offsetParent) == 'object' && objParent.tagName.toUpperCase() != 'BODY') {
			objLeft += objParent.offsetLeft;
			objTop += objParent.offsetTop;
			objParent = objParent.offsetParent;
		}

		var objHeight = obj.offsetHeight;
		var objWidth = obj.offsetWidth;

		if (overDiv.offsetLeft + overDiv.offsetWidth <= objLeft);
		else if (overDiv.offsetTop + overDiv.offsetHeight <= objTop);
		else if (overDiv.offsetTop >= objTop + objHeight);
		else if (overDiv.offsetLeft >= objLeft + objWidth);
		else obj.style.visibility = 'hidden';
	}
}

function showElement(elmID) {
	if (!document.all) return;
	for(i=0; i<document.all.tags(elmID).length; i++) {
		var obj = document.all.tags(elmID)[i];
		if(!obj || !obj.offsetParent) continue;
		obj.style.visibility = '';
	}
}