package com.dev.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.dev.beans.Asset;
import com.dev.beans.AssetAllocation;
import com.dev.beans.AssetStatus;
import com.dev.beans.Employee;
import com.dev.beans.UserMaster;
import com.dev.exceptions.AddAssetException;
import com.dev.exceptions.AddEmployeeException;
import com.dev.exceptions.InvalidDepartmentException;
import com.dev.exceptions.LoginException;
import com.dev.exceptions.RaiseAllocationException;
import com.dev.exceptions.RemoveAssetException;
import com.dev.exceptions.SetStatusExcption;
import com.dev.exceptions.StatusException;
import com.dev.exceptions.UpdateAssetException;

@Repository
public class DAOImpl implements DAO {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory = null;

	Asset asset = new Asset();
	UserMaster um = new UserMaster();

	@Override
	public UserMaster login(Integer userid, String password) {
		entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {

			String jpql = "select usertype from UserMaster where userid=:uid and userpassword=:upwd";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("uid", userid);
			query.setParameter("upwd", password);
			String lst = (String) query.getSingleResult();
			if (lst == null) {
				throw new LoginException();
			} else {
				um.setUsertype(lst);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return um;
	}

	@Override
	public AssetAllocation raiseRequest(AssetAllocation asetallocate) {
		entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			Integer min = 2000;
			Integer max = 5000;
			Integer rand = (int) ((Math.random() * ((max - min) + 1)) + min);
			asetallocate.setAllocationid(rand);

			AssetAllocation assetallocation1 = entityManager.find(AssetAllocation.class,
					asetallocate.getAllocationid());
			if (assetallocation1 == null) {
				EntityTransaction entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				entityManager.persist(asetallocate);
				AssetStatus assetstatus = new AssetStatus();
				assetstatus.setAllocationid(asetallocate.getAllocationid());
				assetstatus.setStatus(null);
				entityManager.persist(assetstatus);
				entityTransaction.commit();
				return asetallocate;
			} else {
				throw new RaiseAllocationException();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return null;
	}

	@Override
	public Employee addEmployee(Employee emp) {
		entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {

			Employee employee1 = entityManager.find(Employee.class, emp.getEmpno());
			if (employee1 == null) {

				String q = "select deptid from Department ";
				Query query = entityManager.createQuery(q);
				@SuppressWarnings({ "unchecked" })
				List<Integer> list = query.getResultList();
				Integer f = 0;
				for (Integer dpt : list) {
					if (emp.getDeptid() == dpt) {
						f = 1;
					}
				}
				if (f != 1) {
					System.out.println("Inavalid Dept id");
					entityManager.close();
					throw new InvalidDepartmentException();
				} else {
					EntityTransaction entityTransaction = entityManager.getTransaction();
					entityTransaction.begin();
					entityManager.persist(emp);
					entityTransaction.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddEmployeeException();

		}
		entityManager.close();
		return emp;
	}

	@Override
	public Asset addAsset(Asset asset) {
		entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {

			Asset asset1 = entityManager.find(Asset.class, asset.getAssetid());
			if (asset1 == null) {
				EntityTransaction entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				entityManager.persist(asset);
				entityTransaction.commit();
				return asset;
			}

			else {
				throw new AddAssetException();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		entityManager.close();
		return asset;
	}

	@Override
	public Asset removeAsset(int r) {
		try {

			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			EntityTransaction entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			String jpql = "Delete from Asset where assetid=:asi";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("asi", r);
			Integer i = query.executeUpdate();

			if (i > 0) {
				entityTransaction.commit();
				System.out.println("Asset Deleted");
				return asset;
			} else {
				throw new RemoveAssetException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String viewStatus(Integer id1) {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			String jpql = "from AssetStatus where allocationid=:aid";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("aid", id1);
			@SuppressWarnings("unchecked")
			List<AssetStatus> list = query.getResultList();
			for (AssetStatus stud : list) {
				System.out.println(stud.getAllocationid());
				if (stud.getStatus() == null) {
					return "Status not updated till now";
				} else {
					return ("status: " + stud.getStatus());
				}
			}
		} catch (Exception e) {
			throw new StatusException();
		}
		return "Enter valid allocation ID";
	}

	@Override
	public Asset updateAsset(int a, Asset asu) {
		Asset asset = new Asset();
		if (asu.getAssetname() != null) {

			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			try {

				EntityTransaction entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				String jpql = "UPDATE Asset SET assetname=:asname WHERE assetid=:aid";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("asname", asu.getAssetname());
				query.setParameter("aid", a);
				Integer i = query.executeUpdate();
				System.out.println("update");
				entityTransaction.commit();
				System.out.println("Query issued");
				if (i > 0) {
					System.out.println("Name Updated");
					return asset;
				} else {
					throw new UpdateAssetException();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			entityManager.close();
		}

		if (asu.getAssetdes() != null) {

			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			try {

				EntityTransaction entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				String jpql = "UPDATE Asset SET assetdes=:asdes WHERE assetid=:aid";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("asdes", asu.getAssetdes());
				query.setParameter("aid", a);
				Integer i = query.executeUpdate();
				System.out.println("update");
				entityTransaction.commit();
				System.out.println("Query issued");
				if (i > 0) {
					System.out.println("Asset Description updated");
					return asset;
				} else {
					throw new UpdateAssetException();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			entityManager.close();
		}
		if (asu.getQuantity() != null) {

			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			try {

				EntityTransaction entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				String jpql = "UPDATE Asset SET quantity=:asname WHERE assetid=:aid";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("asname", asu.getQuantity());
				query.setParameter("aid", a);
				Integer i = query.executeUpdate();
				System.out.println("update");
				entityTransaction.commit();
				System.out.println("Query issued");
				if (i > 0) {
					System.out.println("Quantity Updated");
					return asset;

				} else {
					throw new UpdateAssetException();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			entityManager.close();
		}

		if (asu.getStatus() != null) {
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			try {

				EntityTransaction entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				String jpql = "UPDATE Asset SET status=:asname WHERE assetid=:aid";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("asname", asu.getStatus());
				query.setParameter("aid", a);
				Integer i = query.executeUpdate();
				System.out.println("update");
				entityTransaction.commit();
				System.out.println("Query issued");
				if (i > 0) {
					System.out.println("Asset Status Updated");
				} else {

					throw new UpdateAssetException();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			entityManager.close();
		}

		return asset;
	}

	@Override
	public List<Asset> getAllDetails() {
		try {

			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			String jpql = "from Asset";
			Query query = entityManager.createQuery(jpql);
			@SuppressWarnings("unchecked")
			List<Asset> list = query.getResultList();
			return list;
//			for (Asset details : list) 
//			{
//				System.out.println("ASSet Id:"+details.getAssetid());                                                                              
//				System.out.println("ASSET name:"+details.getAssetname());
//				System.out.println("ASSET Description:"+details.getAssetdes());
//				System.out.println("ASSET Quantity:"+details.getQuantity());
//				System.out.println("ASSET Status:"+details.getStatus());
//				System.out.println("*********************");
//
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AssetAllocation> getAllAssetAllocation() {
		try {

			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			String jpql = "from AssetAllocation";
			Query query = entityManager.createQuery(jpql);
			@SuppressWarnings("unchecked")
			List<AssetAllocation> list = query.getResultList();
			return list;
//			for (AssetAllocation astallocation : list) 
//			{
//				System.out.println("Allocation Id:"+astallocation.getAllocationid());                                                                              
//				System.out.println("ASSET id:"+astallocation.getAssetid());
//				System.out.println("Employee Number:"+astallocation.getEmpno());
//				System.out.println("Allocation Date:"+astallocation.getAllocationdate());
//				System.out.println("Release Date:"+astallocation.getReleasedate());
//				System.out.println("Quantity :"+astallocation.getQuantity());
//				System.out.println("*********************");
//			}

		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean setStatus(Integer allocationid, String status) {
		try {

			entityManagerFactory = Persistence.createEntityManagerFactory("asset_management_rest");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			EntityTransaction entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			String jpql = "UPDATE AssetStatus SET status=:asname WHERE allocationid=:aid";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("asname", status);
			query.setParameter("aid", allocationid);
			Integer i = query.executeUpdate();
			System.out.println("update");
			entityTransaction.commit();
			System.out.println("Query issued");
			if (i > 0) {
				return true;
			} else {
				return false;

			}
		} catch (Exception e) {
			throw new SetStatusExcption();

		}

	}

}
