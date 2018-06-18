package us.dontcareabout.fx.shared;

import java.util.Comparator;

public interface HasId {
	Comparator<HasId> comparator = new Comparator<HasId>() {
		@Override
		public int compare(HasId o1, HasId o2) {
			return Integer.compare(o1.getId(), o2.getId());
		}
	};

	int getId();
	void setId(int id);
}
