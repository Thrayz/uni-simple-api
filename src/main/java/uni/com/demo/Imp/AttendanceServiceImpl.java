package uni.com.demo.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.com.demo.Entity.Attendance;
import uni.com.demo.Repository.AttendanceRepository;
import uni.com.demo.Service.AttendanceService;

import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance getAttendanceById(long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    @Override
    public Attendance updateAttendance(Attendance attendance, long id) {
        if (attendanceRepository.existsById(id)) {
            attendance.setId(id);
            return attendanceRepository.save(attendance);
        }
        return null;
    }

    @Override
    public void deleteAttendance(long id) {
        attendanceRepository.deleteById(id);
    }
}
