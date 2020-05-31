package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.fragment;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.adapter.WhatsappStatusAdapter;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.FragmentWhatsappImageBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.model.WhatsappStatusModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static androidx.databinding.DataBindingUtil.inflate;

public class WhatsappImageFragment extends Fragment {
    FragmentWhatsappImageBinding binding;

    private File[] allfiles;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    private WhatsappStatusAdapter whatsappStatusAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, R.layout.fragment_whatsapp_image, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        statusModelArrayList = new ArrayList<>();
        getData();
        binding.swiperefresh.setOnRefreshListener(() -> {
            statusModelArrayList = new ArrayList<>();
            getData();
            binding.swiperefresh.setRefreshing(false);
        });

    }

    private void getData() {
        WhatsappStatusModel whatsappStatusModel;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
        File targetDirector = new File(targetPath);
        allfiles = targetDirector.listFiles();
        if (allfiles == null) {
            binding.tvNoResult.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoResult.setVisibility(View.GONE);
        }
        try {
            Arrays.sort(allfiles, (Comparator) (o1, o2) -> {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < allfiles.length; i++) {
                File file = allfiles[i];
                if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
                            Uri.fromFile(file),
                            allfiles[i].getAbsolutePath(),
                            file.getName());
                    statusModelArrayList.add(whatsappStatusModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList);
        binding.rvFileList.setAdapter(whatsappStatusAdapter);

    }
}