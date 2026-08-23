package hr.algebra.gamearena.api.service.notification;

import hr.algebra.gamearena.api.dto.notification.NotificationUnreadCountView;
import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationEditRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationFullView;
import hr.algebra.gamearena.api.dto.notification.NotificationMinimalView;
import hr.algebra.gamearena.api.dto.notification.NotificationUnreadAndCountView;
import hr.algebra.gamearena.api.exceptions.extenders.InvalidVariableException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.notification.Notification;
import hr.algebra.gamearena.api.model.notification.NotificationSave;
import hr.algebra.gamearena.api.model.notification.NotificationUpdate;
import hr.algebra.gamearena.api.repository.notification.INotificationRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService{
    private final INotificationRepo notificationRepo;
    private final NotificationPushService notificationPushService;

    private String notificationNotFound(Long id){
        return "Notification with id " + id + " not found";
    }

    public NotificationService(INotificationRepo notificationRepo, NotificationPushService notificationPushService) {
        this.notificationRepo = notificationRepo;
        this.notificationPushService = notificationPushService;
    }

    @Override
    public Optional<NotificationMinimalView> findById(Long id) {
        return Optional.of(notificationRepo.findById(id)
                .map(NotificationMinimalView::fromNotification)
                .orElseThrow(() -> new NotFoundException( notificationNotFound(id) )));
    }

    @Override
    public List<NotificationMinimalView> findAllByUserIdUnreadByDescendingDate(Long userId) {
        return notificationRepo.findUnreadByUserIdByDescendingDate(userId)
                .stream()
                .map(NotificationMinimalView::fromNotification)
                .toList();
    }

    @Override
    public List<NotificationMinimalView> findAllByUserIdByDescendingDate(Long userId) {
        return notificationRepo.findAllByUserIdByDescendingDate(userId)
                .stream()
                .map(NotificationMinimalView::fromNotification)
                .toList();
    }

    @Override
    public NotificationUnreadCountView countUnreadByUserId(Long userId) {
        return NotificationUnreadCountView.fromNotification(notificationRepo.countUnreadByUserIdByDescendingDate(userId));
    }

    @Override
    public NotificationMinimalView createAndPush(NotificationCreateRequest request) {
        if ((request.getReferenceId() == null) != (request.getReferenceType() == null)) {
            throw new InvalidVariableException("referenceId and referenceType must both be provided or both be omitted");
        }

        var notificationSave = new NotificationSave();
        notificationSave.setType(request.getType().toNotificationType());
        notificationSave.setRecipientUserId(request.getRecipientUserId());
        notificationSave.setReferenceId(request.getReferenceId());
        notificationSave.setReferenceType(request.getReferenceType() != null ? request.getReferenceType().toReferenceType() : null);

        var created = NotificationMinimalView.fromNotification(notificationRepo.save(notificationSave));

        notificationPushService.push(request.getRecipientUserId(), buildFullSnapshot(request.getRecipientUserId()));


        return created;
    }

    @Override
    public Optional<NotificationMinimalView> updateAndPush(Long id, NotificationEditRequest request) {
        var notificationUpdate = new NotificationUpdate();
        notificationUpdate.setRead(request.getRead());

        var updated = Optional.of(notificationRepo.update(id, notificationUpdate));

        if(updated.isEmpty()) {
            throw new NotFoundException(notificationNotFound(id));
        }

        notificationPushService.push(updated.get().receiverUserId(), buildFullSnapshot(updated.get().receiverUserId()));

        return Optional.of(NotificationMinimalView.fromNotification(updated.get()));
    }

    @Override
    public void deleteByIdAndPush(Long id){
        var recipientUserId = notificationRepo.findById(id)
                .map(Notification::receiverUserId)
                .orElseThrow(() -> new NotFoundException(notificationNotFound(id)));

        notificationRepo.delete(id);
        notificationPushService.push(recipientUserId, buildFullSnapshot(recipientUserId));
    }

    @Override
    public boolean doesUserOwnNotification(Long userId, Long notificationId) {
        return !notificationRepo.doesUserOwnThisNotification(userId, notificationId);
    }

    @Override
    public Flux<NotificationUnreadAndCountView> streamForUserUnreadAndCount(Long userId) {
        return Flux.concat(
                Flux.just(buildUnreadSnapshot(userId)),
                notificationPushService.subscribe(userId).map(this::toUnreadAndCount));
    }

    @Override
    public Flux<NotificationUnreadCountView> streamToUserUnreadCount(Long userId) {
        return Flux.concat(
                Flux.just(countUnreadByUserId(userId)),
                notificationPushService.subscribe(userId)
                        .map(all -> NotificationUnreadCountView.fromNotification(
                                (int) all.stream().filter(n -> !n.read()).count())));
    }

    @Override
    public Flux<List<NotificationFullView>> streamForUserAll(Long userId) {
        return Flux.concat(Flux.just(buildFullSnapshot(userId)), notificationPushService.subscribe(userId));
    }

    private NotificationUnreadAndCountView toUnreadAndCount(List<NotificationFullView> all) {
        var unread = all.stream()
                .filter(n -> !n.read())
                .map(NotificationFullView::toMinimalView)
                .toList();

        return new NotificationUnreadAndCountView(unread.size(), unread);
    }

    private NotificationUnreadAndCountView buildUnreadSnapshot(Long userId) {
        var unread = notificationRepo.findUnreadByUserIdByDescendingDate(userId)
                .stream()
                .map(NotificationMinimalView::fromNotification)
                .toList();

        return new NotificationUnreadAndCountView(unread.size(), unread);
    }

    private List<NotificationFullView> buildFullSnapshot(Long userId) {
        return notificationRepo.findAllByUserIdByDescendingDate(userId)
                .stream()
                .map(NotificationFullView::fromNotification)
                .toList();
    }
}
