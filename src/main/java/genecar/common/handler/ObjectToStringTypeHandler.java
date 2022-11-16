package genecar.common.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import genecar.common.util.CmmnUtil;

public class ObjectToStringTypeHandler implements TypeHandler<Object> {
	
	private static final Logger logger = LoggerFactory.getLogger(ObjectToStringTypeHandler.class);

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		try {
			if (parameter != null) {
				ps.setString(i, parameter.getClass().getName() + "@" + parameter.toString());
			} else {
				ps.setNull(i, Types.VARCHAR);
			}
		} catch (Exception e) {
			logger.error("ObjectToStringTypeHandler.setParameter : ", e);
		}
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		Object obj = null;
		String[] strArr = null;
		try {
			if (rs != null && rs.getString(columnName) != null) {
				strArr = rs.getString(columnName).split("@", 2);
				obj = CmmnUtil.OM.readValue(strArr[1], Class.forName(strArr[0]));
			}
		} catch (JsonProcessingException | ClassNotFoundException e) {
			logger.error("ObjectToStringTypeHandler.getResult : ", e);
		}
		return obj;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object obj = null;
		String[] strArr = null;
		try {
			if (rs != null && rs.getString(columnIndex) != null) {
				strArr = rs.getString(columnIndex).split("@", 2);
				obj = CmmnUtil.OM.readValue(strArr[1], Class.forName(strArr[0]));
			}
		} catch (JsonProcessingException | ClassNotFoundException e) {
			logger.error("ObjectToStringTypeHandler.getResult : ", e);
		}
		return obj;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		Object obj = null;
		String[] strArr = null;
		try {
			if (cs != null && cs.getString(columnIndex) != null) {
				strArr = cs.getString(columnIndex).split("@", 2);
				obj = CmmnUtil.OM.readValue(strArr[1], Class.forName(strArr[0]));
			}
		} catch (JsonProcessingException | ClassNotFoundException e) {
			logger.error("ObjectToStringTypeHandler.getResult : ", e);
		}
		return obj;
	}

}
