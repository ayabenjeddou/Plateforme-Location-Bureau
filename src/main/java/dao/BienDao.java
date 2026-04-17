package dao;

import java.time.LocalDateTime;
import java.util.List;
import model.Bien;

public interface BienDao {

    Bien findById(Long id);

    List<Bien> findAll();

    void save(Bien bien);

    void delete(Bien bien);

	List<Bien> findDisponibles();

	static List<Bien> findAvailable(LocalDateTime debut, LocalDateTime fin, Integer capaciteMin, String equipements) {
		// TODO Auto-generated method stub
		return null;
	}
}