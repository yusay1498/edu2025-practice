-- 演習1
SELECT
    top + "right" + bottom + "left" AS sum_point
FROM
    card
LIMIT
    10;

-- 演習2
SELECT
    level,
    MAX(top + "right" + bottom + "left") AS max_sum
FROM
    card
GROUP BY
    level
ORDER BY
    level;

-- 演習3
SELECT
    card.id,
    card.name,
    card.level,
    (card.top + card.right + card.left + card.bottom)
FROM
    card
INNER JOIN (
    SELECT
        level,
        MAX(top + "right" + "left" + bottom) AS max_sum
    FROM
        card
    GROUP BY
        level
) card_max
ON
    card.level = card_max.level
AND
    (card.top + card.right + card.left + card.bottom) = card_max.max_sum
ORDER BY
    card.level,
    card.id;