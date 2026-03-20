package repository;

public interface FeeRepository {
	Object[] getFeeDetails(Integer doctorId, Integer userId);
}
