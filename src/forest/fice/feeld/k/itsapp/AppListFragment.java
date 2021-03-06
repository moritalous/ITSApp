package forest.fice.feeld.k.itsapp;

import static forest.fice.feeld.k.itsapp.util.Util.nonNull;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.samples.apps.iosched.util.LogUtils;

import forest.fice.feeld.k.itsapp.DetailActivity.DetailFragment;
import forest.fice.feeld.k.itsapp.entity.FeedEntity;
import forest.fice.feeld.k.itsapp.entity.FeedEntityFactory;
import forest.fice.feeld.k.itsapp.entity.FeedEntity.Entry;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class AppListFragment extends ListFragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
//	private String mParam2;

	private RequestQueue mQueue;

//	private OnFragmentInteractionListener mListener;

	private FeedEntity entity;

	// TODO: Rename and change types of parameters
	public static AppListFragment newInstance(String param1, String param2) {
		AppListFragment fragment = new AppListFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AppListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle;

		if (savedInstanceState != null) {
			bundle = savedInstanceState;
		} else if (getArguments() != null) {
			bundle = getArguments();
		} else {
			bundle = new Bundle();
		}

		mParam1 = bundle.getString(ARG_PARAM1);
//		mParam2 = bundle.getString(ARG_PARAM2);

		mQueue = Volley.newRequestQueue(getActivity());
		mQueue.add(new StringRequest(mParam1, new Listener<String>() {

			@Override
			public void onResponse(String jsonString) {
				entity = FeedEntityFactory.getEntity(jsonString);
				if (entity == null) {
					LogUtils.LOGW("JSON", "Json Parse Error");
				}

				ListAdapter arrayAdapter = new ListAdapter(getActivity(), 0, 0,
						entity.feed.entry, mQueue);

				setListAdapter(arrayAdapter);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
			}
		}));

		// TODO: Change Adapter to display your content
		// setListAdapter(new ArrayAdapter<AppContent.AppItem>(getActivity(),
		// android.R.layout.simple_list_item_1, android.R.id.text1,
		// AppContent.ITEMS));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
//		mListener = null;
		mQueue.cancelAll(new RequestFilter() {
			
			@Override
			public boolean apply(Request<?> arg0) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// if (null != mListener) {
		// // Notify the active callbacks interface (the activity, if the
		// // fragment is attached to one) that an item has been selected.
		// mListener.onFragmentInteraction("");
		// }

		Entry entry = entity.feed.entry[position];

		Intent intent = new Intent(getActivity(), DetailActivity.class);

		intent.putExtra(DetailFragment.ARG_NAME, nonNull(entry.name.text));
		intent.putExtra(DetailFragment.ARG_ARTIST, nonNull(entry.artist.text));
		intent.putExtra(DetailFragment.ARG_PRICE, nonNull(entry.price.text));
		intent.putExtra(DetailFragment.ARG_IMAGE,
				nonNull(entry.image[entry.image.length - 1].text));
		intent.putExtra(DetailFragment.ARG_LINK,
				nonNull(entry.link.attributes.href));
		intent.putExtra(DetailFragment.ARG_CATEGORY,
				nonNull(entry.category.attributes.label));
		if (entry.summary != null) {
			intent.putExtra(DetailFragment.ARG_SUMMARY,
					nonNull(entry.summary.text));
		}

		startActivity(intent);

		// getActivity().getSupportFragmentManager().beginTransaction()
		// .replace(R.id.container, DetailFragment.newInstance(entry))
		// .commit();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState = getArguments();
		super.onSaveInstanceState(outState);
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(String id);
	}
}
