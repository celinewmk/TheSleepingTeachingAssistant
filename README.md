## TheSleepingTeachingAssistant

Le département informatique d'une université dispose d'un assistant d'enseignement (TA)
qui aide les étudiants de premier cycle à effectuer leurs travaux de programmation
pendant les heures de bureau habituelles. Le bureau du TA est assez petit et ne peut
accueillir qu'un seul bureau avec une chaise et un ordinateur. Il y a trois chaises dans le
couloir à l'extérieur du bureau où les étudiants peuvent s'asseoir et attendre si le TA aide
actuellement un autre étudiant. Lorsqu'aucun étudiant n'a besoin d'aide pendant les
heures de bureau, le TA s'assoit à son bureau et fait une sieste. Si un étudiant arrive
pendant les heures de bureau et trouve le TA endormi, l'étudiant doit réveiller le TA pour
lui demander de l'aide. Si un étudiant arrive et trouve le TA en train d'aider un autre
étudiant, l'étudiant s'assoit sur une des chaises dans le couloir et attend. Si aucune chaise
n'est disponible, l'étudiant reviendra plus tard.
En utilisant des fils POSIX, des verrous mutex et des sémaphores, mettez en place une
solution qui coordonne les activités du TA et des étudiants. Les détails de ce travail sont
fournis ci-dessous.


### Les étudiants et le TA
En utilisant les Pthreads (ou threads Java/python), commencez par créer n étudiants.
Chacun d'entre eux exécutera un fil de discussion distinct. Le TA s'exécutera également
comme un fil séparé. Les fils des étudiants alterneront entre la programmation pendant
un certain temps et la recherche d'aide auprès du TA. Si le TA est disponible, ils
obtiendront de l'aide. Sinon, ils s'assoiront sur une chaise dans le couloir ou, si aucune
chaise n'est disponible, reprendront la programmation et demanderont de l'aide
ultérieurement. Si un étudiant arrive et remarque que le TA dort, il doit en informer le
TA à l'aide d'un sémaphore. Lorsque le TA a fini d'aider un étudiant, il doit vérifier s'il y
a des étudiants qui attendent de l'aide dans le couloir. Si c'est le cas, le TA doit aider
chacun de ces étudiants à tour de rôle. Si aucun étudiant n'est présent, le TA peut
recommencer à faire la sieste.
La meilleure option pour simuler la programmation des étudiants - ainsi que le TA qui
aide un étudiant - est peut-être de faire dormir les fils appropriés pendant une période de
temps aléatoire. Ajustez le nombre d’étudiants et le temps de programmation pour que
la simulation soit intéressante.
