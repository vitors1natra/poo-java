package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ProductsDao;
import model.entities.Seller;
import model.entities.Products;

public class ProductsDaoJDBC implements ProductsDao {

	private Connection conn;
	
	public ProductsDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Products obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO products "
					+ "(Name, Category, ReleaseDate, Price, SellerId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getCategory());
			st.setDate(3, new java.sql.Date(obj.getReleaseDate().getTime()));
			st.setDouble(4, obj.getPrice());
			st.setInt(5, obj.getSeller().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Products obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Products "
					+ "SET Name = ?, Category = ?, ReleaseDate = ?, Price = ?, SellerId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getCategory());
			st.setDate(3, new java.sql.Date(obj.getReleaseDate().getTime()));
			st.setDouble(4, obj.getPrice());
			st.setInt(5, obj.getSeller().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM products WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Products findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT products.*,seller.Name as SelName "
					+ "FROM products INNER JOIN seller "
					+ "ON products.SellerId = seller.Id "
					+ "WHERE products.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Seller sel = instantiateSeller(rs);
				Products obj = instantiateProducts(rs, sel);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Products instantiateProducts(ResultSet rs, Seller sel) throws SQLException {
		Products obj = new Products();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setCategory(rs.getString("Category"));
		obj.setReleaseDate(new java.util.Date(rs.getTimestamp("ReleaseDate").getTime()));
		obj.setPrice(rs.getDouble("Price"));
		obj.setSeller(sel);
		return obj;
	}

	private Seller instantiateSeller(ResultSet rs) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("SellerId"));
		sel.setName(rs.getString("SelName"));
		return sel;
	}

	@Override
	public List<Products> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT products.*,seller.Name as SelName "
					+ "FROM products INNER JOIN seller "
					+ "ON products.SellerId = seller.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Products> list = new ArrayList<>();
			Map<Integer, Seller> map = new HashMap<>();
			
			while (rs.next()) {
				
				Seller sel = map.get(rs.getInt("SellerId"));
				
				if (sel == null) {
					sel = instantiateSeller(rs);
					map.put(rs.getInt("SellerId"), sel);
				}
				
				Products obj = instantiateProducts(rs, sel);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Products> findBySeller(Seller seller) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT products.*,seller.Name as SelName "
					+ "FROM products INNER JOIN seller "
					+ "ON products.SellerId = seller.Id "
					+ "WHERE SellerId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, seller.getId());
			
			rs = st.executeQuery();
			
			List<Products> list = new ArrayList<>();
			Map<Integer, Seller> map = new HashMap<>();
			
			while (rs.next()) {
				
				Seller sel = map.get(rs.getInt("SellerId"));
				
				if (sel == null) {
					sel = instantiateSeller(rs);
					map.put(rs.getInt("SellerId"), sel);
				}
				
				Products obj = instantiateProducts(rs, sel);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
