# CribEDGE - Pre-alpha

## Explanation
My dad's side of the family holds an annual summer Family Reunion vacation for a whole week. This year, while on vacation, my dad taught me how to play Cribbage. He then mentioned that my grandma (his mother) was the best player he knew.

Challenge: build a Cribbage-playing bot good enough to beat my grandma.

(It doesn't have to be perfect. It just has to work well *enough.*)

**Update 6/26/2022**\
Back from family reunion. The bot beat my dad twice and my grandma once! Success! Unfortunately, it then suffered a loss against my uncle Cliff. He managed to uncover a failure case in which the bot decided to throw away a 5 into the opponent's crib (Cliff was the dealer that turn), even though I *knew* I'd negatively weighted doing that.\
\
It turned out my 15% difference cutoff idea was flawed. If I remember correctly, I think I may have had a Ace, 2, 3, 4, 5, and 9 that round. The bot decided to keep the Ace, 2, 3, and 9 (for a run of 3 and a 15). The better move would have been to keep the 2, 3, 4, 5 (for a run of 4, but at least you're not throwing away a 5 now).\
\
After thinking it through, I realized that any hands worth 4 wouldn't have even been *considered* for bonus points in that round, because the 15% cutoff was too small. The bot considered the hand worth 5 points to be the only option. I am now removing the percent-difference cutoff system, in favor of a composite-score sort, in an attempt to lend a little more consideration to the bonus points.

## Record
75% - 3 wins / 1 loss

## Roadmap
**Pre-alpha**
- [ ] Add pegging section
- [ ] Add hand worth recalculation with turn card
- [ ] Add crib worth calculation (with turn card, of course)
- [ ] Add opponent hand worth calculation (with turn card)
- [ ] Add opponent crib worth calculation (with turn card)
- [ ] Add scorekeeping, with auto-repeat until win or loss

**Alpha**
- [ ] Design playing card images
- [ ] Implement GUI card selection
- [ ] Implement GUI worth reporting
- [ ] Design pegboard and pegs images
- [ ] Implement virtual-pegboard scorekeeping

**Beta**

At this stage, all that might remain would be gradual strategy improvement, spurred by observations during use and possibly community suggestions/contributions.