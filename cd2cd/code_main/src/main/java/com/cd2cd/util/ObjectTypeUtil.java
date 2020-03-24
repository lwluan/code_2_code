package com.cd2cd.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.internal.types.JdbcTypeNameTranslator;

import com.mysql.jdbc.StringUtils;

public class ObjectTypeUtil {

	public static IntrospectedColumn getIntrospectedColumn(String typeInfo) {
		int columnSize = 0;
		int decimalDigits = 0;

		String javaType = typeInfo;
		int ei = javaType.indexOf("(");
		javaType = javaType.substring(0, ei>-1?ei:javaType.length());
		
		// 需要转一下 org.mybatis.generator.internal.types.JdbcTypeNameTranslator
		String javaTypeStr = javaType.toUpperCase();
		if(javaTypeStr.equals("INT")) {
			javaTypeStr = "INTEGER";
		}
		
		if (StringUtils.startsWithIgnoreCase(typeInfo, "enum")) {
			String temp = typeInfo.substring(typeInfo.indexOf("("), typeInfo.lastIndexOf(")"));
			java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(temp, ",");
			int maxLength = 0;

			while (tokenizer.hasMoreTokens()) {
				maxLength = Math.max(maxLength, (tokenizer.nextToken().length() - 2));
			}

			columnSize = maxLength;
			decimalDigits = 0;
		} else if (StringUtils.startsWithIgnoreCase(typeInfo, "set")) {
			String temp = typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.lastIndexOf(")"));
			java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(temp, ",");
			int maxLength = 0;

			int numElements = tokenizer.countTokens();

			if (numElements > 0) {
				maxLength += (numElements - 1);
			}

			while (tokenizer.hasMoreTokens()) {
				String setMember = tokenizer.nextToken().trim();

				if (setMember.startsWith("'") && setMember.endsWith("'")) {
					maxLength += setMember.length() - 2;
				} else {
					maxLength += setMember.length();
				}
			}

			columnSize = maxLength;
		} else if (typeInfo.indexOf(",") != -1) {
			// Numeric with decimals
			columnSize = Integer.valueOf(typeInfo.substring((typeInfo.indexOf("(") + 1), (typeInfo.indexOf(","))).trim());
			decimalDigits = Integer.valueOf(typeInfo.substring((typeInfo.indexOf(",") + 1), (typeInfo.indexOf(")"))).trim());
		} else {

			boolean isUnsigned = StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "unsigned");
			/* If the size is specified with the DDL, use that */
			if ((StringUtils.indexOfIgnoreCase(typeInfo, "char") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "text") != -1
					|| StringUtils.indexOfIgnoreCase(typeInfo, "blob") != -1 || StringUtils.indexOfIgnoreCase(typeInfo, "binary") != -1 || StringUtils
					.indexOfIgnoreCase(typeInfo, "bit") != -1) && typeInfo.indexOf("(") != -1) {
				int endParenIndex = typeInfo.indexOf(")");

				if (endParenIndex == -1) {
					endParenIndex = typeInfo.length();
				}

				columnSize = Integer.valueOf(typeInfo.substring((typeInfo.indexOf("(") + 1), endParenIndex).trim());

			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyint")) {
				if (typeInfo.indexOf("(1)") != -1) {
					columnSize = 1;
					decimalDigits = 0;
				} else {
					columnSize = 3;
					decimalDigits = 0;
				}
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "smallint")) {
				columnSize = 5;
				decimalDigits = 0;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumint")) {
				columnSize = isUnsigned ? 8 : 7;
				decimalDigits = 0;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int")) {
				columnSize = 10;
				decimalDigits = 0;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "integer")) {
				columnSize = 10;
				decimalDigits = 0;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "bigint")) {
				columnSize = isUnsigned ? 20 : 19;
				decimalDigits = 0;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int24")) {
				columnSize = 19;
				decimalDigits = 0;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "real")) {
				columnSize = 12;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "float")) {
				columnSize = 12;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "decimal")) {
				columnSize = 12;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "numeric")) {
				columnSize = 12;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "double")) {
				columnSize = 22;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "char")) {
				columnSize = 1;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "varchar")) {
				columnSize = 255;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "timestamp")) {
				columnSize = 19;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "datetime")) {
				columnSize = 19;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "date")) {
				columnSize = 10;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "time")) {
				columnSize = 8;

			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyblob")) {
				columnSize = 255;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "blob")) {
				columnSize = 65535;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumblob")) {
				columnSize = 16777215;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longblob")) {
				columnSize = Integer.MAX_VALUE;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinytext")) {
				columnSize = 255;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "text")) {
				columnSize = 65535;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumtext")) {
				columnSize = 16777215;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longtext")) {
				columnSize = Integer.MAX_VALUE;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "enum")) {
				columnSize = 255;
			} else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "set")) {
				columnSize = 255;
			}
		}

		// DECIMAL 3
		int type = JdbcTypeNameTranslator.getJdbcType(javaTypeStr);
		IntrospectedColumn introspectedColumn = new IntrospectedColumn();
		introspectedColumn.setJdbcType(type);
		introspectedColumn.setLength(columnSize);
		introspectedColumn.setScale(decimalDigits);
		
		return introspectedColumn;
	}

}
