<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:firmware="http://www.bidib.org/schema/firmware">
	<xsl:output method="html" encoding="UTF-8" />



	<xsl:template match="firmware:Firmware">
		<html>
			<head>
				<title>BiDiB Firmware</title>
			</head>
			<body>
				<h2>BiDiB Firmware</h2>

				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="firmware:Node[@xsi:type='DeviceNode']">
		<br />
		<table border="1" rules="all" cellpadding="2" cellspacing="0"
			width="90%">
			<THEAD>
				<tr>
					<TH colspan="3" align="left" STYLE="font-size:14pt" bgcolor="LightGrey">Device
						Node</TH>
				</tr>
				<tr>
					<TH align="center" STYLE="font-size:14pt" width="100">VID</TH>
					<TH align="center" STYLE="font-size:14pt" width="100">PID</TH>
					<TH align="left" STYLE="font-size:14pt">Beschreibung</TH>
				</tr>
			</THEAD>
			<TBODY>
				<tr>
					<TD align="center" STYLE="font-size:14pt">
						<xsl:value-of select="@VID" />
					</TD>
					<TD align="center" STYLE="font-size:14pt">
						<xsl:value-of select="@PID" />
					</TD>
					<TD STYLE="font-size:14pt">
<!-- 						<xsl:value-of select="@Comment" /> -->
						<xsl:for-each select="firmware:Nodetext[@Lang='de-DE']">
							<xsl:value-of select="@Text" />
						</xsl:for-each>
						
					</TD>
				</tr>
			</TBODY>
		</table>
		<table>
			<tbody>
				<tr>
					<td width="50"></td>
					<td><xsl:apply-templates /></td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="firmware:Node[@xsi:type='FirmwareNode']">
		<table border="1" rules="all" cellpadding="2" cellspacing="0"
			width="90%">
				<!-- 
			<THEAD>
				<tr>
					<TH colspan="3" align="left" STYLE="font-size:14pt" bgcolor="LightGrey">Firmware Datei</TH>
				</tr>
				<tr>
					<TH STYLE="font-size:12pt" width="400">Description</TH>
					<TH STYLE="font-size:12pt">Filename</TH>
				</tr>
			</THEAD>
				 -->
			<TBODY>
				<tr>
					<TD width="400" STYLE="font-size:12pt">
						<xsl:for-each select="firmware:Nodetext[@Lang='de-DE']">
							<xsl:value-of select="@Text" />
						</xsl:for-each>
					</TD>
					<TD width="400" STYLE="font-size:12pt">
						<xsl:value-of select="firmware:Filename" />
					</TD>
				</tr>
			</TBODY>
		</table>
	</xsl:template>

	<xsl:template match="firmware:Version">
		<table border="1" rules="all" cellpadding="2" cellspacing="0"
			width="90%">
			<THEAD>
				<tr bgcolor="LightGrey">
					<TH align="center" STYLE="font-size:14pt">Version</TH>
					<TH align="center" STYLE="font-size:14pt">Beschreibung</TH>
					<TH align="center" STYLE="font-size:14pt">Author</TH>
				</tr>
			</THEAD>
			<TBODY>
				<tr>
					<TD align="center" STYLE="font-size:14pt">
						<xsl:value-of select="@Version" />
					</TD>
					<TD align="center" STYLE="font-size:14pt">
						<xsl:value-of select="@Description" />
					</TD>
					<TD align="center" STYLE="font-size:14pt">
						<xsl:value-of select="@Author" />
					</TD>
				</tr>
			</TBODY>
		</table>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="firmware:FirmwareDefinition">
		<xsl:apply-templates />
	</xsl:template>

</xsl:stylesheet>



