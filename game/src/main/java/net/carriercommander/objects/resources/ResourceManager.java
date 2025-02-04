package net.carriercommander.objects.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResourceManager {
	private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);
	private final List<ResourceContainer> containers = new ArrayList<>();

	public int getAmount(Class<ResourceItem> resourceClass) {
		Optional<ResourceContainer> resourceOptional = findContainer(resourceClass);
		return resourceOptional.isPresent() ? resourceOptional.get().getAmount() : 0;
	}

	public boolean add(Class<ResourceItem> resourceClass, int amount) {
		Optional<ResourceContainer> resourceOptional = findContainer(resourceClass);
		if (resourceOptional.isPresent()) {
			return resourceOptional.get().add(amount);
		} else {
			ResourceContainer container = null;
			try {
				container = new ResourceContainer(resourceClass.getDeclaredConstructor().newInstance(), 0);
			} catch (Exception e) {
				logger.error("unable to instantiate resource", e);
			}
			addContainer(container);
			return container.add(amount);
		}
	}

	private Optional<ResourceContainer> findContainer(Class<ResourceItem> resourceClass) {
		return containers.stream()
				.filter(resourceContainer -> resourceContainer.getItem().getClass().equals(resourceClass)).findFirst();
	}

	public ResourceContainer getContainer(int index) {
		return containers.get(index);
	}

	public final List<ResourceContainer> getContainers() {
		return containers;
	}

	public void addContainer(ResourceContainer container) {
		containers.add(container);
	}

	public void addContainer(List<ResourceContainer> containers) {
		this.containers.addAll(containers);
	}
}
