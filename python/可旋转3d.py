import matplotlib.pyplot as plt
import numpy as np
from scipy import array

jet = plt.get_cmap('jet')
fig = plt.figure()
ax = fig.gca(projection='3d')
X = np.linspace(70, 40, 4)
Y = np.linspace(5, 2, 4)
X, Y = np.meshgrid(X, Y)
Z = array([
    [1223.539555, 1428.075086, 1714.479425, 2144.053223],
    [1567.26647, 1829.056119, 2990.416079, 2745.320067],
    [2135.163957, 2491.534201, 2990.416079, 3738.761638],
    [3257.280827, 3800.655101, 4561.372117, 5702.458776],
])
surf = ax.plot_surface(X, Y, Z, rstride=1, cstride=1, cmap=jet, linewidth=0, alpha=1)
ax.set_zlim3d(0, Z.max())
fig.colorbar(surf, shrink=0.8, aspect=5)
ax.set_xlabel('Axial Length [mm]')
ax.set_ylabel('nbTurns')
ax.set_zlabel('RPM')
plt.show()
