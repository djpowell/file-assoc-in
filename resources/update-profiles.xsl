<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="value" />

    <!-- parameter already exists -->
    <xsl:template mode="insert-parameter-value" name="insert-parameter-value" match="/root/map/map[preceding-sibling::keyword/name[text() = 'user']][1]/string[preceding-sibling::keyword[name[text() = 'java-cmd']][1]]/text()">
        <xsl:value-of select="$value" />
    </xsl:template>

    <!-- :user profile already exists -->
    <xsl:template mode="insert-parameter" name="insert-parameter" match="/root/map/map[preceding-sibling::keyword/name[text() = 'user']][1]/node()[1]">
        <xsl:text>{</xsl:text>
        <newline><xsl:text>
</xsl:text></newline>
        <whitespace><xsl:text>  </xsl:text></whitespace>
        <keyword>
            <xsl:text>:</xsl:text>
            <name>java-cmd</name>
        </keyword>
        <whitespace><xsl:text> </xsl:text></whitespace>
        <string>
            <xsl:call-template name="insert-parameter-value" />
        </string>
        <newline><xsl:text>
</xsl:text>
</newline>
        <whitespace><xsl:text>  </xsl:text></whitespace>
    </xsl:template>

    <!-- no :user profile -->
    <xsl:template mode="insert-user-profile" name="insert-user-profile" match="/root/map[1]/node()[1]">
        <xsl:text>{</xsl:text>
        <keyword>:<name>user</name></keyword>
        <newline><xsl:text>
</xsl:text>
</newline>
        <whitespace><xsl:text>  </xsl:text></whitespace>
        <map>
            <xsl:call-template name="insert-parameter" />
            <newline><xsl:text>
</xsl:text>
</newline>
            <xsl:text>}</xsl:text>
        </map>
        <newline><xsl:text>
</xsl:text>
</newline>
    </xsl:template>

    <!-- no profile map -->
    <xsl:template mode="insert-profile-map" name="insert-profile-map" match="/root/node()[last()]">
        <newline><xsl:text>
</xsl:text>
</newline>
        <map>
            <xsl:call-template name="insert-user-profile" />
            <newline><xsl:text>
</xsl:text>
</newline>
            <xsl:text>}</xsl:text>
        </map>
    </xsl:template>

    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="/root/map/map[preceding-sibling::keyword/name[text() = 'user']][1]/string[preceding-sibling::keyword[name[text() = 'java-cmd']][1]]/text()">
                <xsl:message>insert-parameter-value</xsl:message>
                <xsl:apply-templates mode="insert-parameter-value" />
            </xsl:when>
            <xsl:when test="/root/map/map[preceding-sibling::keyword/name[text() = 'user']][1]/node()[1]">
                <xsl:message>insert-parameter</xsl:message>
                <xsl:apply-templates mode="insert-parameter" />
            </xsl:when>
            <xsl:when test="/root/map[1]/node()[1]">
                <xsl:message>insert-user-profile</xsl:message>
                <xsl:apply-templates mode="insert-user-profile" />
            </xsl:when>
            <xsl:when test="not(/root/node())">
                <xsl:message>empty-file</xsl:message>
                <root>
                    <xsl:call-template name="insert-profile-map" />
                </root>
            </xsl:when>
            <xsl:otherwise>
                <xsl:message>insert-profile-map</xsl:message>
                <xsl:apply-templates mode="insert-profile-map" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template mode="insert-parameter-value" match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="insert-parameter-value" />
        </xsl:copy>
    </xsl:template>

    <xsl:template mode="insert-parameter" match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="insert-parameter" />
        </xsl:copy>
    </xsl:template>

    <xsl:template mode="insert-user-profile" match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="insert-user-profile" />
        </xsl:copy>
    </xsl:template>

    <xsl:template mode="insert-profile-map" match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="insert-profile-map" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>