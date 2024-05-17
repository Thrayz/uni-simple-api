package uni.com.demo.Service;

import uni.com.demo.Entity.Attendance;

import java.util.List;

public interface AttendanceService {
    Attendance saveAttendance(Attendance attendance);
    List<Attendance> getAllAttendances();
    Attendance getAttendanceById(long id);
    Attendance updateAttendance(Attendance attendance, long id);
    void deleteAttendance(long id);
}
