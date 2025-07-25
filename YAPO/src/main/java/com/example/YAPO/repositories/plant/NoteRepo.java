package com.example.YAPO.repositories.plant;

import com.example.YAPO.models.plant.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {

    void deleteByIdAndPlant_idAndPlant_User_Id(Long plantId, Long idNote, Long id);

    Note findByPlant_IdAndPlantUser_IdAndId(Long plantId, Long plantId1, long id);
}
