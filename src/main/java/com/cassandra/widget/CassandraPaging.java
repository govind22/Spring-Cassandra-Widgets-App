package com.cassandra.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

@Component("cassandrapaging")
public class CassandraPaging {

	private Session session;

	public CassandraPaging(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Retrieve rows for the specified page offset.
	 * 
	 * @param statement
	 * @param page
	 *            starting row (>1), inclusive
	 * @param per_page
	 *            the maximum rows need to retrieve.
	 * @return List<Row>
	 */
	public List<Row> fetchRowsWithPage(Statement statement, int page, int per_page) {
		ResultSet result = skipRows(statement, page, per_page);
		return getRows(result, page, per_page);
	}

	private ResultSet skipRows(Statement statement, int page, int per_page) {
		ResultSet result = null;
		int skippingPages = getPageNumber(page, per_page);
		String savingPageState = null;
		statement.setFetchSize(per_page);
		boolean isEnd = false;
		for (int i = 0; i < skippingPages; i++) {
			if (null != savingPageState) {
				statement = statement.setPagingState(PagingState
						.fromString(savingPageState));
			}
			result = session.execute(statement);
			PagingState pagingState = result.getExecutionInfo()
					.getPagingState();
			if (null != pagingState) {
				savingPageState = result.getExecutionInfo().getPagingState()
						.toString();
			}

			if (result.isFullyFetched() && null == pagingState) {
				// if hit the end more than once, then nothing to return,
				// otherwise, mark the isEnd to 'true'
				if (true == isEnd) {
					return null;
				} else {
					isEnd = true;
				}
			}
		}
		return result;
	}

	private int getPageNumber(int start, int per_page) {
		if (start >= 1) {
			int page = 1;
			if (start > per_page) {
				page = (start - 1) / per_page + 1;
			}
			return page;
		}
		return 0;
	}

	private List<Row> getRows(ResultSet result, int page, int per_page) {
		List<Row> rows = new ArrayList<Row>(per_page);
		if (null == result) {
			return rows;
		}
		int skippingRows = (page - 1) % per_page;
		int index = 0;
		for (Iterator<Row> iter = result.iterator(); iter.hasNext()
				&& rows.size() < per_page;) {
			Row row = iter.next();
			if (index >= skippingRows) {
				rows.add(row);
			}
			index++;
		}
		return rows;
	}

}
