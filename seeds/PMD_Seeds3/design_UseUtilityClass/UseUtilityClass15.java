
public class AccountFragment extends Fragment {

    public static AccountFragment newInstance() {
        AccountFragment instance = new AccountFragment();
        //OTHER STUFF
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mylayout, container, false);
    }
}
        