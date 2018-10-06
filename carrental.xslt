<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/carrental">
    <html>
        <head><title>Car Rental</title></head>
        <body>
            <xsl:apply-templates select="rental"/>
        </body>
    </html>
</xsl:template>
<xsl:template match="rental">
    <fieldset><legend>Car</legend>
    <table border="0">
        Make: <xsl:value-of select="make"/> <br />
        Model: <xsl:value-of select="model"/> <br />
        Days: <xsl:value-of select="nofdays"/> <br />
        Unit: <xsl:value-of select="nofunits"/> <br />
        Discount: <xsl:value-of select="discount"/> <br />
    </table> 
    </fieldset>

</xsl:template>
</xsl:stylesheet>