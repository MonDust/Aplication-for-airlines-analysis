package pg.edu.pl.lsea.udp;

public interface ProgressListener {
    void onProgressUpdate(int processed, int total);
}