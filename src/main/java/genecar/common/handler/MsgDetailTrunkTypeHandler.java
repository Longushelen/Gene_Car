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

import genecar.common.util.CmmnUtil;

public class MsgDetailTrunkTypeHandler implements TypeHandler<Object> {
	
	private static final Logger logger = LoggerFactory.getLogger(MsgDetailTrunkTypeHandler.class);

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		try {
			if (parameter != null) {
				ps.setString(i, CmmnUtil.subStrBytes(parameter.toString(), 3000));
			} else {
				ps.setNull(i, Types.VARCHAR);
			}
		} catch (Exception e) {
			logger.error("MsgDetailTrunkTypeHandler.setParameter : ", e);
		}
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		if (rs != null) return rs.getString(columnName);
		return null;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		if (rs != null) return rs.getString(columnIndex);
		return null;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		if (cs != null) return cs.getString(columnIndex);
		return null;
	}

}
