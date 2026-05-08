import { useState, useRef, useEffect } from 'react';
import { sendMessage } from '../../shared/api/chatbot';
import styles from './ChatbotPage.module.css';

const SUGGESTIONS = [
  '약을 식전에 먹어야 하나요, 식후에 먹어야 하나요?',
  '두 가지 약을 같이 먹어도 괜찮나요?',
  '약을 까먹었을 때 어떻게 해야 하나요?',
  '약 부작용이 나타나면 어떻게 해야 하나요?',
];

function ChatMessage({ message }) {
  const isUser = message.role === 'user';
  return (
    <div className={`${styles.messageRow} ${isUser ? styles.userRow : styles.botRow}`}>
      {!isUser && <span className={styles.avatar}>💊</span>}
      <div className={`${styles.bubble} ${isUser ? styles.userBubble : styles.botBubble}`}>
        {message.text.split('\n').map((line, i) => (
          <span key={i}>
            {line}
            {i < message.text.split('\n').length - 1 && <br />}
          </span>
        ))}
      </div>
    </div>
  );
}

export default function ChatbotPage() {
  const [messages, setMessages] = useState([
    {
      id: 0,
      role: 'bot',
      text: '안녕하세요! 복약 안전 상담 AI MediGuard입니다. 💊\n약 복용에 대해 궁금한 점을 물어보세요.',
    },
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const bottomRef = useRef(null);
  const inputRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, loading]);

  const addMessage = (role, text) =>
    setMessages((prev) => [...prev, { id: Date.now() + Math.random(), role, text }]);

  const handleSend = async (prompt) => {
    const text = prompt ?? input.trim();
    if (!text || loading) return;

    setInput('');
    addMessage('user', text);
    setLoading(true);

    try {
      const { data } = await sendMessage(text);
      addMessage('bot', data.body.text);
    } catch {
      addMessage('bot', '죄송해요, 일시적인 오류가 발생했어요. 잠시 후 다시 시도해주세요.');
    } finally {
      setLoading(false);
      inputRef.current?.focus();
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const showSuggestions = messages.length === 1;

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <span className={styles.headerIcon}>💊</span>
        <div>
          <h1 className={styles.headerTitle}>AI 복약 상담</h1>
          <p className={styles.headerSub}>MediGuard AI</p>
        </div>
      </header>

      <div className={styles.messageList}>
        {messages.map((msg) => (
          <ChatMessage key={msg.id} message={msg} />
        ))}

        {loading && (
          <div className={`${styles.messageRow} ${styles.botRow}`}>
            <span className={styles.avatar}>💊</span>
            <div className={`${styles.bubble} ${styles.botBubble} ${styles.typingBubble}`}>
              <span className={styles.dot} />
              <span className={styles.dot} />
              <span className={styles.dot} />
            </div>
          </div>
        )}

        {showSuggestions && (
          <div className={styles.suggestions}>
            <p className={styles.suggestLabel}>이런 질문은 어떠세요?</p>
            {SUGGESTIONS.map((s) => (
              <button
                key={s}
                className={styles.suggestBtn}
                onClick={() => handleSend(s)}
                disabled={loading}
              >
                {s}
              </button>
            ))}
          </div>
        )}

        <div ref={bottomRef} />
      </div>

      <div className={styles.inputArea}>
        <textarea
          ref={inputRef}
          className={styles.input}
          placeholder="복약 관련 궁금한 점을 입력하세요..."
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          rows={1}
          disabled={loading}
        />
        <button
          className={styles.sendBtn}
          onClick={() => handleSend()}
          disabled={!input.trim() || loading}
          aria-label="전송"
        >
          ↑
        </button>
      </div>
    </div>
  );
}