package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.util.ManualResetEvent;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class ModelEventHandler {
    private MainForm _form;
    private Queue<ModelEvent> _eventQueue;
    private ManualResetEvent _manualResetEvent;

    ModelEventHandler(MainForm form) {
        _form = form;
        _eventQueue = new ConcurrentLinkedQueue<ModelEvent>();
        _manualResetEvent = new ManualResetEvent();

        Thread eventsProcessingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ModelEventHandler.this.processEvents();
            }
        });
        eventsProcessingThread.start();
    }

    private void processEvents() {
        // TODO stop cycle
        while (true) {
            try {
                _manualResetEvent.waitOne();

                if (_eventQueue.isEmpty()) {
                    _manualResetEvent.reset();
                } else {
                    _form.processEvent(_eventQueue.remove());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void addEvent(ModelEvent event) {
        for (ModelEvent existedEvent : _eventQueue) {
            if ((existedEvent.getEventType() == event.getEventType())
                && (existedEvent.getSender() == event.getSender())){
                return;
            }
        }
        _eventQueue.add(event);
        _manualResetEvent.set();
    }
}
